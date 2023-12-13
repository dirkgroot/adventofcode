use crate::utils;

pub fn part1(input: &str) -> usize {
    summarize_maps(input, 0)
}

pub fn part2(input: &str) -> usize {
    summarize_maps(input, 1)
}

fn summarize_maps(input: &str, smudges: usize) -> usize {
    input
        .split("\n\n")
        .map(utils::parse_map)
        .map(|map| summarize(&map, smudges))
        .sum()
}

fn summarize(map: &Vec<Vec<char>>, smudges: usize) -> usize {
    let columns = (0..map[0].len()).map(|x| column(map, x)).collect();
    let vertical = (0..map.len() - 1).find(|y| reflection(map, *y, smudges).is_some());
    let horizontal = (0..map[0].len() - 1).find(|x| reflection(&columns, *x, smudges).is_some());

    match (vertical, horizontal) {
        (Some(y), _) => (y + 1) * 100,
        (_, Some(x)) => x + 1,
        _ => panic!(),
    }
}

fn column(map: &Vec<Vec<char>>, x: usize) -> Vec<char> {
    map.iter().map(|row| row[x]).collect()
}

fn reflection(map: &Vec<Vec<char>>, row: usize, smudges: usize) -> Option<usize> {
    let reference = row + 1;
    let (reflect, diff) = (reference..map.len())
        .take_while(|r| r - reference <= row)
        .map(|r| differences(&map[r], &map[row - (r - reference)]))
        .fold((0, 0), |(r, total_diff), diff| {
            if total_diff + diff <= smudges {
                (r + 1, total_diff + diff)
            } else {
                (r, total_diff)
            }
        });
    if diff == smudges && (reference + reflect == map.len() || reference == reflect) {
        Some(row)
    } else {
        None
    }
}

fn differences(v1: &Vec<char>, v2: &Vec<char>) -> usize {
    v1.iter().zip(v2).filter(|(a, b)| *a != *b).count()
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 13;

    const EXAMPLE: &str = "#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE), 405);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 28651);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE), 400);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 25450);
    }
}
