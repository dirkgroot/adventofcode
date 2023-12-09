use crate::utils;
use crate::year2019::intcode::Intcode;

pub fn part1(input: &str) -> i32 {
    max_thruster_signal(&Intcode::parse(input), &vec![0, 1, 2, 3, 4])
}

pub fn part2(input: &str) -> i32 {
    max_thruster_signal(&Intcode::parse(input), &vec![5, 6, 7, 8, 9])
}

fn max_thruster_signal(program: &Intcode, settings: &Vec<i32>) -> i32 {
    utils::permutations(settings)
        .iter()
        .map(|settings| {
            let mut amplifiers = vec![program.clone(); 5];
            thruster_signal(&mut amplifiers, settings)
        })
        .max()
        .unwrap()
}

fn thruster_signal(programs: &mut Vec<Intcode>, settings: &Vec<i32>) -> i32 {
    let mut output = 0;
    let mut first = true;
    while !programs[0].ready {
        output = programs
            .iter_mut()
            .enumerate()
            .fold(output, |acc, (i, amplifier)| {
                let result = match first {
                    true => amplifier.exec(&vec![settings[i], acc]).unwrap()[0],
                    false => amplifier.exec(&vec![acc]).unwrap()[0],
                };
                result
            });
        first = false;
    }
    output
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2019::read_input;

    use super::*;

    const DAY: i32 = 7;

    const EXAMPLE_1: &str = "3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0";
    const EXAMPLE_2: &str =
        "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0";
    const EXAMPLE_3: &str = "3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0";

    #[test]
    fn part1_examples() {
        assert_eq!(test_support::do_part(part1, EXAMPLE_1), 43210);
        assert_eq!(test_support::do_part(part1, EXAMPLE_2), 54321);
        assert_eq!(test_support::do_part(part1, EXAMPLE_3), 65210);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 17790);
    }

    const EXAMPLE_4: &str =
        "3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5";

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE_4), 139629729);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 19384820);
    }
}
