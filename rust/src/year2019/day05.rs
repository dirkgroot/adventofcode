use crate::year2019::intcode::Intcode;

pub fn part1(input: &str) -> i32 {
    *Intcode::parse(input)
        .exec(&vec![1])
        .unwrap()
        .last()
        .unwrap()
}

pub fn part2(input: &str) -> i32 {
    Intcode::parse(input).exec(&vec![5]).unwrap()[0]
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2019::read_input;

    use super::*;

    const DAY: i32 = 5;

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 6761139);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 9217546);
    }
}
