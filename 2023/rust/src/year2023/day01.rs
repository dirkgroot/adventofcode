pub fn part1(input: &str) -> u32 {
    input
        .lines()
        .map(|line| {
            let digits = find_digits(line).collect::<Vec<_>>();
            solution(digits)
        })
        .sum()
}

pub fn part2(input: &str) -> u32 {
    input
        .lines()
        .map(|line| {
            let digits = find_digits(line);
            let text_digits = find_text_digits(line);

            let mut all_digits = text_digits.chain(digits).collect::<Vec<_>>();
            all_digits.sort_by(|(_, a), (_, b)| a.cmp(b));
            solution(all_digits)
        })
        .sum()
}

fn solution(all_digits: Vec<(u32, usize)>) -> u32 {
    all_digits[0].0 * 10 + all_digits.last().unwrap().0
}

fn find_digits(line: &str) -> impl Iterator<Item = (u32, usize)> + '_ {
    line.char_indices()
        .filter(|(_, c)| c.is_digit(10))
        .map(|(i, c)| (c.to_digit(10).unwrap(), i))
}

fn find_text_digits(line: &str) -> impl Iterator<Item = (u32, usize)> + '_ {
    [
        ("one", 1),
        ("two", 2),
        ("three", 3),
        ("four", 4),
        ("five", 5),
        ("six", 6),
        ("seven", 7),
        ("eight", 8),
        ("nine", 9),
        ("zero", 0),
    ]
    .iter()
    .flat_map(|(s, n)| line.match_indices(s).map(|(x, _)| (*n, x)))
    .map(|(d, i)| (d, i))
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 1;
    const EXAMPLE1: &str = "1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet";
    const EXAMPLE2: &str = "two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE1), 142);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE2), 281);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 55002);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 55093);
    }
}
