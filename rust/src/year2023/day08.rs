use std::collections::HashMap;
use std::iter::{repeat, successors};

use crate::utils;

pub fn part1(input: &str) -> usize {
    let (nodes, instructions) = parse(input);

    path_length("AAA", &nodes, &instructions, |node| node == "ZZZ")
}

pub fn part2(input: &str) -> u64 {
    let (nodes, instructions) = parse(input);
    nodes
        .keys()
        .filter(|it| it.ends_with("A"))
        .map(|start| path_length(*start, &nodes, &instructions, |node| node.ends_with("Z")) as u64)
        .reduce(utils::lcm)
        .unwrap()
}

fn parse(input: &str) -> (HashMap<&str, (&str, &str)>, String) {
    let (instr, nodes) = input.split_once("\n\n").unwrap();
    let nodes = nodes
        .lines()
        .map(|line| (&line[0..=2], (&line[7..=9], &line[12..=14])))
        .collect::<HashMap<_, _>>();
    (nodes, String::from(instr))
}

fn path_length(
    start_node: &str,
    nodes: &HashMap<&str, (&str, &str)>,
    instructions: &str,
    stop_if: fn(&str) -> bool,
) -> usize {
    let mut instructions = repeat(instructions.chars()).flatten();
    let path_length = successors(Some(start_node), |prev| {
        if stop_if(*prev) {
            return None;
        }
        match instructions.next().unwrap() {
            'L' => Some(nodes[*prev].0),
            'R' => Some(nodes[*prev].1),
            _ => panic!(),
        }
    })
    .count();
    path_length - 1
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 8;

    const EXAMPLE_1: &str = "LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)";

    const EXAMPLE_2: &str = "LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE_1), 6);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 12643);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE_2), 6);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(
            test_support::do_part(part2, &read_input(DAY)),
            13133452426987
        );
    }
}
