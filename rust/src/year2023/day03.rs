use std::ops::Range;

use regex::Regex;

use crate::year2023::day03::Element::{Part, Symbol};

enum Element<'a> {
    Part(i32),
    Symbol(&'a str),
}

pub fn part1(input: &str) -> i32 {
    let elements = parse(input);

    elements
        .iter()
        .filter(|(_, _, el)| matches!(el, Part(_)))
        .filter(|(y, xs, _)| {
            elements
                .iter()
                .any(|(ye, xse, el)| matches!(el, Symbol(_)) && is_adjacent(y, xs, ye, xse))
        })
        .map(|(_, _, el)| match el {
            Part(i) => i,
            Symbol(_) => panic!(),
        })
        .fold(0, move |acc, i| acc + i)
}

pub fn part2(input: &str) -> i32 {
    let elements = parse(input);

    elements
        .iter()
        .filter(|(_, _, e)| match *e {
            Part(_) => false,
            Symbol(s) => s == "*",
        })
        .fold(0, |acc, (y1, xs1, _)| {
            let adjacent = elements
                .iter()
                .filter(|(y2, xs2, e2)| matches!(e2, Part(_)) && is_adjacent(y1, xs1, y2, xs2))
                .map(|(_, _, e2)| match e2 {
                    Part(i) => *i,
                    Symbol(_) => panic!(),
                })
                .collect::<Vec<_>>();
            if adjacent.len() == 2 {
                acc + adjacent.iter().product::<i32>()
            } else {
                acc
            }
        })
}

fn parse(input: &str) -> Vec<(usize, Range<usize>, Element)> {
    let re = Regex::new("\\d+|[^\\d.]+").unwrap();
    input
        .lines()
        .enumerate()
        .flat_map(|(y, line)| re.find_iter(line).map(move |m| (y, m.range(), m.as_str())))
        .map(|(y, xs, str)| {
            (
                y,
                xs,
                match str.parse::<i32>() {
                    Ok(i) => Part(i),
                    Err(_) => Symbol(str),
                },
            )
        })
        .collect::<Vec<(usize, Range<usize>, Element)>>()
}

fn is_adjacent(y1: &usize, xs1: &Range<usize>, y2: &usize, xs2: &Range<usize>) -> bool {
    (y1.checked_sub(1).unwrap_or(0)..=y1 + 1).contains(y2)
        && ((xs2.start <= xs1.start && xs2.end >= xs1.start)
        || (xs2.start <= xs1.end - 1 && xs2.end >= xs1.end - 1)
        || xs2.end == xs1.start
        || xs2.start == xs1.end)
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 3;

    const EXAMPLE: &str = "467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE), 4361);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE), 467835);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 535078);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 75312571);
    }
}
