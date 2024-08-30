use std::collections::HashMap;
use std::iter::successors;

pub fn part1(input: &str) -> i32 {
    let map = parse(input);
    map.keys().map(|orbitee| count_orbits(*orbitee, &map)).sum()
}

fn count_orbits(orbiter: &str, map: &HashMap<&str, &str>) -> i32 {
    if orbiter == "COM" {
        0
    } else {
        1 + count_orbits(map[orbiter], map)
    }
}

pub fn part2(input: &str) -> i32 {
    let map = parse(input);
    let you_path = path("YOU", &map);
    let san_path = path("SAN", &map);

    let you_idx = (0usize..you_path.len()).rev();
    let san_idx = (0usize..san_path.len()).rev();
    let overlap = you_idx
        .zip(san_idx)
        .take_while(|(a, b)| you_path[*a] == san_path[*b])
        .count();

    (you_path.len() - overlap + san_path.len() - overlap) as i32
}

fn path(orbiter: &str, map: &HashMap<&str, &str>) -> Vec<String> {
    successors(Some(String::from(map[orbiter])), |prev| {
        map.get(prev.as_str()).map(|o| String::from(*o))
    })
    .collect()
}

fn parse(input: &str) -> HashMap<&str, &str> {
    input
        .lines()
        .map(|l| l.split_once(")").unwrap())
        .map(|(orbitee, orbiter)| (orbiter, orbitee))
        .collect::<HashMap<_, _>>()
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2019::read_input;

    use super::*;

    const DAY: i32 = 6;

    const EXAMPLE_1: &str = "COM)B
B)C
C)D
D)E
E)F
B)G
G)H
D)I
E)J
J)K
K)L";

    const EXAMPLE_2: &str = "COM)B
B)C
C)D
D)E
E)F
B)G
G)H
D)I
E)J
J)K
K)L
K)YOU
I)SAN";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE_1), 42);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 122782);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE_2), 4);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 271);
    }
}
