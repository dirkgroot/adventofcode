use std::ops::Range;

use regex::Regex;

use crate::year2023::day03::ElementType::{Part, Symbol};

pub fn part1(input: &str) -> i32 {
    let elements = parse(input);

    elements.iter().fold(0, |acc, Element(y, xs, et)| match et {
        Part(i) if has_adjacent_symbol(y, xs, &elements) => acc + i,
        _ => acc,
    })
}

pub fn part2(input: &str) -> i32 {
    let elements = parse(input);

    elements
        .iter()
        .fold(0, |acc, Element(y, xs, et)| match *et {
            Symbol(s) if s == "*" => acc + gear_ratio(y, xs, &elements),
            _ => acc,
        })
}

fn parse(input: &str) -> Vec<Element> {
    let re = Regex::new("\\d+|[^\\d.]").unwrap();
    input
        .lines()
        .enumerate()
        .flat_map(|(y, line)| re.find_iter(line).map(move |m| (y, m.range(), m.as_str())))
        .map(|(y, xs, str)| {
            let element_type = match str.parse::<i32>() {
                Ok(i) => Part(i),
                Err(_) => Symbol(str),
            };
            Element(y, xs, element_type)
        })
        .collect::<Vec<_>>()
}

fn has_adjacent_symbol(y: &usize, xs: &Range<usize>, elements: &Vec<Element>) -> bool {
    elements
        .iter()
        .any(|Element(y2, xs2, et)| matches!(et, Symbol(_)) && is_adjacent(y, xs, y2, xs2))
}

fn gear_ratio(y1: &usize, xs1: &Range<usize>, elements: &Vec<Element>) -> i32 {
    let adjacent = elements
        .iter()
        .filter_map(|Element(y2, xs2, e2)| match e2 {
            Part(i) if is_adjacent(y1, xs1, y2, xs2) => Some(*i),
            _ => None,
        })
        .collect::<Vec<_>>();
    if adjacent.len() == 2 {
        adjacent.iter().product::<i32>()
    } else {
        0
    }
}

fn is_adjacent(y1: &usize, xs1: &Range<usize>, y2: &usize, xs2: &Range<usize>) -> bool {
    (y1.checked_sub(1).unwrap_or(0)..=y1 + 1).contains(y2)
        && ((xs2.start <= xs1.start && xs2.end >= xs1.start)
            || (xs2.start <= xs1.end - 1 && xs2.end >= xs1.end - 1)
            || xs2.end == xs1.start
            || xs2.start == xs1.end)
}

enum ElementType<'a> {
    Part(i32),
    Symbol(&'a str),
}

struct Element<'a>(usize, Range<usize>, ElementType<'a>);

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
