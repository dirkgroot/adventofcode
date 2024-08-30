use crate::utils::parse_map;
use std::cmp::{max, min};

pub fn part1(input: &str) -> i64 {
    sum_distances(&parse_map(input), 2)
}

pub fn part2(input: &str) -> i64 {
    sum_distances(&parse_map(input), 1000000)
}

fn sum_distances(map: &Vec<Vec<char>>, expansion: usize) -> i64 {
    let galaxies = galaxies(map);
    let empty_cols = empty_columns(&map);
    let empty_rows = empty_rows(&map);
    let expansion = expansion - 1;

    let mut sum = 0i64;
    for i in 0..(galaxies.len() - 1) {
        for j in (i + 1)..galaxies.len() {
            let (y1, x1) = galaxies[i];
            let (y2, x2) = galaxies[j];
            let add_y = (min(y1, y2)..max(y1, y2))
                .filter(|y| empty_rows.contains(&y))
                .count()
                * expansion;
            let add_x = (min(x1, x2)..max(x1, x2))
                .filter(|x| empty_cols.contains(&x))
                .count()
                * expansion;
            sum += (max(x1, x2) + add_x) as i64 - min(x1, x2) as i64 + (max(y1, y2) + add_y) as i64
                - min(y1, y2) as i64;
        }
    }
    sum
}

fn galaxies(map: &Vec<Vec<char>>) -> Vec<(usize, usize)> {
    map.iter()
        .enumerate()
        .flat_map(|(y, row)| {
            row.iter()
                .enumerate()
                .filter(|(_, cell)| **cell == '#')
                .map(move |(x, _)| (y, x))
        })
        .collect()
}

fn empty_columns(map: &Vec<Vec<char>>) -> Vec<usize> {
    (0..map[0].len())
        .filter(|x| map.iter().all(move |row| row[*x] == '.'))
        .rev()
        .collect()
}

fn empty_rows(map: &Vec<Vec<char>>) -> Vec<usize> {
    map.iter()
        .enumerate()
        .filter(|(_, cells)| cells.iter().all(|cell| *cell == '.'))
        .map(|(y, _)| y)
        .rev()
        .collect()
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 11;

    const EXAMPLE: &str = "...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE), 374);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 9274989);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE), 82000210);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 357134560737);
    }
}
