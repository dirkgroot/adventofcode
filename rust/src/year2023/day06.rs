pub fn part1(input: &str) -> i64 {
    let mut lines = input.lines();
    let time = parse_line_1(lines.next().unwrap());
    let distance = parse_line_1(lines.next().unwrap());

    time.iter()
        .zip(distance)
        .map(|(t, d)| ways_to_beat(*t, d))
        .product()
}

fn parse_line_1(distance: &str) -> Vec<i64> {
    let numbers_str = distance.split_whitespace().skip(1);
    numbers_str.map(|t| t.parse::<i64>().unwrap()).collect()
}

pub fn part2(input: &str) -> i64 {
    let mut lines = input.lines();
    let time = parse_line_2(lines.next().unwrap());
    let distance = parse_line_2(lines.next().unwrap());

    ways_to_beat(time, distance)
}

fn parse_line_2(time: &str) -> i64 {
    let (_, number) = time.split_once(":").unwrap();
    number.replace(" ", "").parse::<i64>().unwrap()
}

fn ways_to_beat(time: i64, distance: i64) -> i64 {
    (1..time).filter(|t| t * (time - t) > distance).count() as i64
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 6;

    const EXAMPLE: &str = "Time:      7  15   30
Distance:  9  40  200";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE), 288);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 32076);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE), 71503);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 34278221);
    }
}
