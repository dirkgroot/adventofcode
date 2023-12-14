use std::collections::HashMap;

use crate::utils;

pub fn part1(input: &str) -> usize {
    let mut map = utils::parse_map(input);
    tilt_n(&mut map);
    total_load(&map)
}

pub fn part2(input: &str) -> usize {
    let mut map = utils::parse_map(input);
    let mut cache: HashMap<String, i32> = HashMap::new();
    let mut cycle_start = 0;
    let mut cycle_length = 0;

    for i in 1.. {
        cycle(&mut map);
        let cache_key = map_string(&map);
        if let Some(j) = cache.get(&cache_key) {
            cycle_start = *j;
            cycle_length = j - i;
            break;
        }
        cache.insert(cache_key, i);
    }

    let remaining_cycles = (1_000_000_000 - cycle_start) % cycle_length;
    for _ in 0..remaining_cycles {
        cycle(&mut map);
    }

    total_load(&map)
}

fn cycle(map: &mut Vec<Vec<char>>) {
    tilt_n(map);
    tilt_w(map);
    tilt_s(map);
    tilt_e(map);
}

fn tilt_n(map: &mut Vec<Vec<char>>) {
    (0..map.len()).for_each(|y| {
        (0..map[0].len()).for_each(|x| {
            if map[y][x] == '.' {
                let next_round = (y + 1..map.len())
                    .take_while(|yo| map[*yo][x] != '#')
                    .find(|yo| map[*yo][x] == 'O');
                if let Some(yo) = next_round {
                    map[y][x] = 'O';
                    map[yo][x] = '.';
                }
            }
        })
    });
}

fn tilt_w(map: &mut Vec<Vec<char>>) {
    (0..map[0].len()).for_each(|x| {
        (0..map.len()).for_each(|y| {
            if map[y][x] == '.' {
                let next_round = (x + 1..map[0].len())
                    .take_while(|xo| map[y][*xo] != '#')
                    .find(|xo| map[y][*xo] == 'O');
                if let Some(xo) = next_round {
                    map[y][x] = 'O';
                    map[y][xo] = '.';
                }
            }
        })
    });
}

fn tilt_s(map: &mut Vec<Vec<char>>) {
    (0..map.len()).rev().for_each(|y| {
        (0..map[0].len()).for_each(|x| {
            if map[y][x] == '.' {
                let next_round = (0..y)
                    .rev()
                    .take_while(|yo| map[*yo][x] != '#')
                    .find(|yo| map[*yo][x] == 'O');
                if let Some(yo) = next_round {
                    map[y][x] = 'O';
                    map[yo][x] = '.';
                }
            }
        })
    });
}

fn tilt_e(map: &mut Vec<Vec<char>>) {
    (0..map[0].len()).rev().for_each(|x| {
        (0..map.len()).for_each(|y| {
            if map[y][x] == '.' {
                let next_round = (0..x)
                    .rev()
                    .take_while(|xo| map[y][*xo] != '#')
                    .find(|xo| map[y][*xo] == 'O');
                if let Some(xo) = next_round {
                    map[y][x] = 'O';
                    map[y][xo] = '.';
                }
            }
        })
    });
}

fn map_string(map: &Vec<Vec<char>>) -> String {
    let mut result = String::from("");
    map.iter()
        .for_each(|row| row.iter().for_each(|c| result.push(*c)));
    result
}

fn total_load(map: &Vec<Vec<char>>) -> usize {
    map.iter()
        .enumerate()
        .map(|(y, row)| row.iter().filter(|c| **c == 'O').count() * (map.len() - y))
        .sum()
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 14;

    const EXAMPLE: &str = "O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE), 136);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 108889);
    }

    #[test]
    fn test_load() {
        let map = utils::parse_map(
            "OOOO.#.O..
OO..#....#
OO..O##..O
O..#.OO...
........#.
..#....#.#
..O..#.O.O
..O.......
#....###..
#....#....",
        );
        assert_eq!(total_load(&map), 136);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE), 64);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 104671);
    }
}
