use std::iter;

use crate::year2019::intcode::Intcode;

pub fn part1(input: &str) -> i64 {
    let mut program = Intcode::parse(input);

    program.set(1, 12);
    program.set(2, 2);

    match program.exec(&vec![]) {
        Ok(_) => program.get(0),
        Err(msg) => panic!("{}", msg),
    }
}

pub fn part2(input: &str) -> i64 {
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
        match attempt.exec(&vec![]) {
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
