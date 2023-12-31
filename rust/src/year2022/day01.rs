use std::collections::BinaryHeap;

pub fn part1(input: &str) -> i32 {
    parse(input).max().unwrap()
}

pub fn part2(input: &str) -> i32 {
    parse(input)
        .collect::<BinaryHeap<i32>>()
        .iter()
        .take(3)
        .sum()
}

fn parse(input: &str) -> impl Iterator<Item = i32> + '_ {
    input.split("\n\n").map(total_energy)
}

fn total_energy(group: &str) -> i32 {
    group.lines().map(|line| line.parse::<i32>().unwrap()).sum()
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2022::read_input;

    use super::*;

    const DAY: i32 = 1;

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 69836);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 207968);
    }
}
