pub fn part1(input: &str) -> i32 {
    parse(input)
}

pub fn part2(input: &str) -> i32 {
    parse(input)
}

fn parse(input: &str) -> i32 {
    input.parse::<i32>().unwrap()
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2022::read_input;

    use super::*;

    const DAY: i32 = 0;

    #[test]
    fn part1_solution() {
        assert_eq!(
            test_support::do_part(part1, &read_input(DAY)),
            2022
        );
    }

    #[test]
    fn part2_solution() {
        assert_eq!(
            test_support::do_part(part2, &read_input(DAY)),
            2022
        );
    }
}
