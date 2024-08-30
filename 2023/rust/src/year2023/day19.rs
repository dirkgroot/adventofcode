use std::cmp::{max, min};
use std::collections::HashMap;

pub fn part1(input: &str) -> u64 {
    let (workflows, parts) = parse(input);
    let start_wf = &workflows["in"];
    parts
        .iter()
        .filter_map(|part| match start_wf.process(part, &workflows) {
            Action::ACCEPT => Some(part),
            _ => None,
        })
        .map(Part::rating)
        .sum()
}

pub fn part2(input: &str) -> u64 {
    let (workflows, _) = parse(input);
    let start_wf = &workflows["in"];
    let mut stack = Vec::new();

    start_wf.combinations(&workflows, &mut stack, 0)
}

fn parse(input: &str) -> (HashMap<&str, Workflow>, Vec<Part>) {
    let (workflows, parts) = input.split_once("\n\n").unwrap();
    let workflows = workflows
        .lines()
        .map(Workflow::parse)
        .map(|wf| (wf.name, wf))
        .collect();
    let parts = parts.lines().map(Part::parse).collect();
    (workflows, parts)
}

struct Workflow<'a> {
    name: &'a str,
    steps: Vec<Step<'a>>,
}

impl<'a> Workflow<'a> {
    fn parse(input: &'a str) -> Self {
        let (name, rest) = input.split_once("{").unwrap();
        let rest = &rest[..rest.len() - 1];
        let steps = rest.split(",").map(Step::parse).collect();
        Self { name, steps }
    }

    fn process(&'a self, part: &'a Part, workflows: &'a HashMap<&str, Workflow>) -> Action {
        let step = self
            .steps
            .iter()
            .find(|step| step.condition.matches(part))
            .unwrap();
        match step.action {
            Action::MOVE(other) => workflows[other].process(part, workflows),
            _ => step.action,
        }
    }

    fn combinations(
        &'a self,
        workflows: &'a HashMap<&str, Workflow>,
        stack: &mut Vec<Condition<'a>>,
        step_idx: usize,
    ) -> u64 {
        let step = &self.steps[step_idx];
        stack.push(step.condition);
        let match_result = match step.action {
            Action::MOVE(other) => workflows[other].combinations(workflows, stack, 0),
            Action::ACCEPT => calculate_combinations(&stack),
            Action::REJECT => 0,
        };
        stack.pop();
        let no_match_result = if step.condition != Condition::NONE {
            stack.push(step.condition.negate());
            let i1 = self.combinations(workflows, stack, step_idx + 1);
            stack.pop();
            i1
        } else {
            0
        };
        match_result + no_match_result
    }
}

fn calculate_combinations(stack: &Vec<Condition>) -> u64 {
    let mut ranges = HashMap::from([
        ("x", (1u64, 4001u64)),
        ("m", (1u64, 4001u64)),
        ("a", (1u64, 4001u64)),
        ("s", (1u64, 4001u64)),
    ]);

    for c in stack {
        match c {
            Condition::LT(prop, i) => {
                ranges.insert(prop, (ranges[prop].0, min(ranges[prop].1, *i)));
            }
            Condition::GT(prop, i) => {
                ranges.insert(prop, (max(ranges[prop].0, *i + 1), ranges[prop].1));
            }
            Condition::NONE => {}
        }
    }
    ranges.values().map(|(a, b)| b - a).product()
}

struct Step<'a> {
    condition: Condition<'a>,
    action: Action<'a>,
}

impl<'a> Step<'a> {
    fn parse(input: &'a str) -> Self {
        let (condition, action) = if let Some((condition, action)) = input.split_once(":") {
            (Condition::parse(condition), Action::parse(action))
        } else {
            (Condition::NONE, Action::parse(input))
        };
        Self { condition, action }
    }
}

#[derive(Clone, Copy, PartialEq)]
enum Condition<'a> {
    LT(&'a str, u64),
    GT(&'a str, u64),
    NONE,
}

impl<'a> Condition<'a> {
    fn parse(input: &'a str) -> Self {
        let comparison = if input.contains("<") { "<" } else { ">" };
        let (prop, number) = input.split_once(comparison).unwrap();
        let number = number.parse().unwrap();
        match comparison {
            "<" => Condition::LT(prop, number),
            ">" => Condition::GT(prop, number),
            _ => panic!(),
        }
    }

    fn matches(&self, part: &Part) -> bool {
        match self {
            Condition::LT(prop, val) => part.props[prop] < *val,
            Condition::GT(prop, val) => part.props[prop] > *val,
            Condition::NONE => true,
        }
    }

    fn negate(&self) -> Condition {
        match self {
            Condition::LT(prop, i) => Condition::GT(prop, i - 1),
            Condition::GT(prop, i) => Condition::LT(prop, i + 1),
            Condition::NONE => Condition::NONE,
        }
    }
}

#[derive(Copy, Clone)]
enum Action<'a> {
    MOVE(&'a str),
    ACCEPT,
    REJECT,
}

impl<'a> Action<'a> {
    fn parse(input: &'a str) -> Self {
        match input {
            "A" => Action::ACCEPT,
            "R" => Action::REJECT,
            _ => Action::MOVE(input),
        }
    }
}

struct Part<'a> {
    props: HashMap<&'a str, u64>,
}

impl<'a> Part<'a> {
    fn parse(input: &'a str) -> Self {
        let input = &input[1..input.len() - 1];
        let props = input
            .split(",")
            .map(|p| {
                let (prop, value) = p.split_once("=").unwrap();
                (prop, value.parse().unwrap())
            })
            .collect();
        Self { props }
    }

    fn rating(&self) -> u64 {
        self.props.values().sum()
    }
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 19;

    const EXAMPLE: &str = "px{a<2006:qkq,m>2090:A,rfg}
pv{a>1716:R,A}
lnx{m>1548:A,A}
rfg{s<537:gd,x>2440:R,A}
qs{s>3448:A,lnx}
qkq{x<1416:A,crn}
crn{x>2662:A,R}
in{s<1351:px,qqz}
qqz{s>2770:qs,m<1801:hdj,R}
gd{a>3333:R,R}
hdj{m>838:A,pv}

{x=787,m=2655,a=1222,s=2876}
{x=1679,m=44,a=2067,s=496}
{x=2036,m=264,a=79,s=2244}
{x=2461,m=1339,a=466,s=291}
{x=2127,m=1623,a=2188,s=1013}";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE), 19114);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 377025);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE), 167409079868000);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(
            test_support::do_part(part2, &read_input(DAY)),
            135506683246673
        );
    }
}
