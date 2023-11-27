use std::iter;

pub fn part1(input: &str) -> i32 {
    let mut program = Intcode::parse(input);

    program.set(1, 12);
    program.set(2, 2);

    match program.exec() {
        Ok(_) => program.get(0),
        Err(msg) => panic!("{}", msg),
    }
}

pub fn part2(input: &str) -> i32 {
    let program = Intcode::parse(input);

    let candidates = iter::successors(Some((0, 0)), move |prev| match prev {
        (noun, _) if *noun == 100 => None,
        (noun, verb) if *verb == 100 => Some((*noun + 1, 0)),
        (noun, verb) => Some((*noun, verb + 1)),
    });
    for (noun, verb) in candidates {
        let mut attempt = program.clone();
        attempt.set(1, noun);
        attempt.set(2, verb);
        match attempt.exec() {
            Ok(_) => {
                if attempt.get(0) == 19690720 {
                    return 100 * noun + verb;
                }
            }
            Err(_) => {}
        }
    }
    0
}

struct Intcode {
    code: Vec<i32>,
}

impl Intcode {
    fn parse(input: &str) -> Intcode {
        Intcode {
            code: input
                .split(",")
                .map(|i| i.parse::<i32>().unwrap())
                .collect::<Vec<i32>>(),
        }
    }

    fn get(&self, index: usize) -> i32 {
        self.code[index]
    }

    fn set(&mut self, index: usize, value: i32) {
        self.code[index] = value;
    }

    fn clone(&self) -> Intcode {
        Intcode {
            code: self.code.clone(),
        }
    }

    fn exec(&mut self) -> Result<(), String> {
        let mut ip = 0;
        loop {
            match self.code[ip] {
                1 => {
                    let i1 = self.code[self.code[ip + 1] as usize];
                    let i2 = self.code[self.code[ip + 2] as usize];
                    let destination = self.code[ip + 3] as usize;
                    self.code[destination] = i1 + i2;
                    ip += 4;
                }
                2 => {
                    let i1 = self.code[self.code[ip + 1] as usize];
                    let i2 = self.code[self.code[ip + 2] as usize];
                    let destination = self.code[ip + 3] as usize;
                    self.code[destination] = i1 * i2;
                    ip += 4;
                }
                99 => return Ok(()),
                _ => return Err(String::from("Invalid program!")),
            }
        }
    }
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2019::read_input;

    use super::*;

    const DAY: i32 = 2;

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 6730673);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 3749);
    }
}
