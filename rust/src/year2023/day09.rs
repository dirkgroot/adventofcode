pub fn part1(input: &str) -> i32 {
    find_next(input, false)
}

pub fn part2(input: &str) -> i32 {
    find_next(input, true)
}

fn find_next(input: &str, reverse: bool) -> i32 {
    parse(input, reverse)
        .map(|history| next_value(&history))
        .sum()
}

fn parse(input: &str, reverse: bool) -> impl Iterator<Item = Vec<i32>> + '_ {
    input.lines().map(move |line| {
        let map = line.split_whitespace().map(|n| n.parse::<i32>().unwrap());
        match reverse {
            true => map.rev().collect(),
            false => map.collect(),
        }
    })
}

fn next_value(prev: &Vec<i32>) -> i32 {
    match prev.iter().all(|d| *d == 0) {
        true => 0,
        false => prev.last().unwrap() + next_value(&prev.windows(2).map(|w| w[1] - w[0]).collect()),
    }
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 9;

    const EXAMPLE: &str = "0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE), 114);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 1757008019);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE), 2);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 995);
    }
}
