use std::collections::{HashMap, HashSet, VecDeque};

use crate::utils;

pub fn part1(input: &str) -> u64 {
    let mut config = ModuleConfig::parse(input);

    for _ in 0..1000 {
        config.push_button(None);
    }

    config.high_pulses * config.low_pulses
}

pub fn part2(input: &str) -> u64 {
    let mut config = ModuleConfig::parse(input);
    let mut monitor = HashMap::new();

    while monitor.len() < 4 {
        let highs = config.push_button(Some("lv"));

        highs.iter().for_each(|src| {
            monitor.insert(src.clone(), config.button_presses);
        });
    }

    monitor.values().map(|i| *i).reduce(utils::lcm).unwrap()
}

struct ModuleConfig {
    modules: HashMap<String, Box<dyn Module>>,
    high_pulses: u64,
    low_pulses: u64,
    button_presses: u64,
}

impl ModuleConfig {
    fn parse(input: &str) -> Self {
        let mods = input
            .lines()
            .map(|line| {
                let (type_id, destinations) = line.split_once(" -> ").unwrap();
                let destinations = destinations
                    .split(", ")
                    .map(|it| it.to_string())
                    .collect::<Vec<_>>();
                let (id, module) = match type_id.chars().next() {
                    Some('%') => (Some('%'), &type_id[1..]),
                    Some('&') => (Some('&'), &type_id[1..]),
                    _ => (None, type_id),
                };
                (module, (id, destinations))
            })
            .collect::<HashMap<&str, (Option<char>, Vec<String>)>>();
        let modules = mods
            .iter()
            .map(|(id, (mod_type, destinations))| {
                let module = match mod_type {
                    Some('%') => {
                        Box::new(FlipFlop::new(*id, destinations.clone())) as Box<dyn Module>
                    }
                    Some('&') => {
                        let sources = mods
                            .iter()
                            .filter(|(_, (_, d))| d.contains(&id.to_string()))
                            .map(|(id, _)| id.to_string())
                            .collect::<Vec<_>>();
                        Box::new(Conjunction::new(*id, destinations.clone(), sources))
                            as Box<dyn Module>
                    }
                    _ => Box::new(Broadcast::new(*id, destinations.clone())) as Box<dyn Module>,
                };
                (id.to_string(), module)
            })
            .collect();

        Self {
            modules,
            high_pulses: 0,
            low_pulses: 0,
            button_presses: 0,
        }
    }

    fn push_button(&mut self, monitor: Option<&str>) -> HashSet<String> {
        let mut queue = VecDeque::new();
        let mut highs: HashSet<String> = HashSet::new();

        self.button_presses += 1;
        queue.push_back(Pulse::new(PulseType::Low, "", "broadcaster"));

        while let Some(pulse) = queue.pop_front() {
            if let Some(monitor) = monitor {
                if pulse.destination == monitor && pulse.pulse_type == PulseType::High {
                    highs.insert(pulse.source.clone());
                }
            }

            if let Some(module) = self.modules.get_mut(pulse.destination.as_str()) {
                if let Some(output) = module.process_pulse(&pulse) {
                    for pulse in output {
                        queue.push_back(pulse);
                    }
                }
            }

            if pulse.pulse_type == PulseType::High {
                self.high_pulses += 1;
            } else {
                self.low_pulses += 1;
            }
        }
        highs
    }
}

trait Module {
    fn process_pulse(&mut self, pulse: &Pulse) -> Option<Vec<Pulse>>;
}

struct Broadcast {
    id: String,
    destinations: Vec<String>,
}

impl Broadcast {
    fn new(id: &str, destinations: Vec<String>) -> Self {
        Self {
            id: id.to_string(),
            destinations,
        }
    }
}

impl Module for Broadcast {
    fn process_pulse(&mut self, pulse: &Pulse) -> Option<Vec<Pulse>> {
        Some(
            self.destinations
                .iter()
                .map(|dst| Pulse::new(pulse.pulse_type, &self.id, dst))
                .collect(),
        )
    }
}

struct FlipFlop {
    id: String,
    on: bool,
    destinations: Vec<String>,
}

impl FlipFlop {
    pub fn new(id: &str, destinations: Vec<String>) -> Self {
        Self {
            id: id.to_string(),
            on: false,
            destinations,
        }
    }
}

impl Module for FlipFlop {
    fn process_pulse(&mut self, pulse: &Pulse) -> Option<Vec<Pulse>> {
        match pulse.pulse_type {
            PulseType::High => None,
            PulseType::Low => {
                self.on = !self.on;
                let pulse_type = match self.on {
                    true => PulseType::High,
                    false => PulseType::Low,
                };
                Some(
                    self.destinations
                        .iter()
                        .map(|dst| Pulse::new(pulse_type, &self.id, dst))
                        .collect(),
                )
            }
        }
    }
}

struct Conjunction {
    id: String,
    inputs: HashMap<String, PulseType>,
    destinations: Vec<String>,
}

impl Conjunction {
    fn new(id: &str, destinations: Vec<String>, sources: Vec<String>) -> Self {
        Self {
            id: id.to_string(),
            inputs: sources
                .iter()
                .map(|i| (i.clone(), PulseType::Low))
                .collect(),
            destinations,
        }
    }
}

impl Module for Conjunction {
    fn process_pulse(&mut self, pulse: &Pulse) -> Option<Vec<Pulse>> {
        self.inputs.insert(pulse.source.clone(), pulse.pulse_type);
        let pulse_type = if self.inputs.iter().all(|(_, pt)| *pt == PulseType::High) {
            PulseType::Low
        } else {
            PulseType::High
        };
        Some(
            self.destinations
                .iter()
                .map(|dst| Pulse::new(pulse_type, &self.id, dst))
                .collect(),
        )
    }
}

struct Pulse {
    pulse_type: PulseType,
    source: String,
    destination: String,
}

impl Pulse {
    fn new(pulse_type: PulseType, source: &str, destination: &str) -> Self {
        Self {
            pulse_type,
            source: source.to_string(),
            destination: destination.to_string(),
        }
    }
}

#[derive(PartialEq, Copy, Clone)]
enum PulseType {
    High,
    Low,
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 20;

    const EXAMPLE1: &str = "broadcaster -> a, b, c
%a -> b
%b -> c
%c -> inv
&inv -> a";
    const EXAMPLE2: &str = "broadcaster -> a
%a -> inv, con
&inv -> b
%b -> con
&con -> output";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE1), 32000000);
        assert_eq!(test_support::do_part(part1, EXAMPLE2), 11687500);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 812721756);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(
            test_support::do_part(part2, &read_input(DAY)),
            233338595643977
        );
    }
}
