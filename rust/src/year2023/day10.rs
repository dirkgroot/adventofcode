use crate::utils::{parse_map, Direction};
use std::iter::successors;

pub fn part1(input: &str) -> usize {
    main_loop(&parse_map(input)).len() / 2
}

/// Calculate the number of inner cells using Pick's theorem.
/// See https://en.wikipedia.org/wiki/Pick%27s_theorem.
pub fn part2(input: &str) -> usize {
    let main_loop = main_loop(&parse_map(input));

    area(&main_loop) - (main_loop.len() / 2) + 1
}

fn main_loop(map: &Vec<Vec<char>>) -> Vec<(usize, usize)> {
    let start_y = map.iter().position(|row| row.contains(&'S')).unwrap();
    let start_x = map[start_y].iter().position(|c| *c == 'S').unwrap();
    let direction = start_direction(map, start_y, start_x);

    successors(Some((start_y, start_x, direction)), |(y, x, direction)| {
        let (new_y, new_x) = match direction {
            Direction::N => (y - 1, *x),
            Direction::E => (*y, x + 1),
            Direction::S => (y + 1, *x),
            Direction::W => (*y, x - 1),
        };
        let dir = match (direction, map[new_y][new_x]) {
            (Direction::N, '7') => Direction::W,
            (Direction::N, '|') => Direction::N,
            (Direction::N, 'F') => Direction::E,
            (Direction::E, '-') => Direction::E,
            (Direction::E, 'J') => Direction::N,
            (Direction::E, '7') => Direction::S,
            (Direction::S, '|') => Direction::S,
            (Direction::S, 'L') => Direction::E,
            (Direction::S, 'J') => Direction::W,
            (Direction::W, '-') => Direction::W,
            (Direction::W, 'F') => Direction::S,
            (Direction::W, 'L') => Direction::N,
            _ => return None,
        };
        Some((new_y, new_x, dir))
    })
    .map(|(y, x, _)| (y, x))
    .collect()
}

fn start_direction(map: &Vec<Vec<char>>, y: usize, x: usize) -> Direction {
    if y > 0 && vec!['|', '7', 'F'].contains(&map[y - 1][x]) {
        Direction::N
    } else if x < (map[0].len() - 1) && vec!['-', '7', 'J'].contains(&map[y][x + 1]) {
        Direction::E
    } else if y < (map.len() - 1) && vec!['|', 'J', 'L'].contains(&map[y + 1][x]) {
        Direction::S
    } else if x > 0 && vec!['-', 'L', 'F'].contains(&map[y][x - 1]) {
        Direction::W
    } else {
        panic!()
    }
}

/// Calculate the area of a polygon using the Shoelace formula.
/// See https://en.wikipedia.org/wiki/Shoelace_formula.
fn area(path: &Vec<(usize, usize)>) -> usize {
    let mut area = 0i32;
    let mut j = path.len() - 1;
    (0usize..path.len()).for_each(|i| {
        area += (path[j].1 as i32 + path[i].1 as i32) * (path[j].0 as i32 - path[i].0 as i32);
        j = i;
    });
    (area / 2).abs() as usize
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 10;

    const EXAMPLE_1: &str = "7-F7-
.FJ|7
SJLL7
|F--J
LJ.LJ";

    const EXAMPLE_2: &str = ".F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ...";

    const EXAMPLE_3: &str = "..........
.S------7.
.|F----7|.
.||....||.
.||....||.
.|L-7F-J|.
.|..||..|.
.L--JL--J.
..........";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE_1), 8);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 6800);
    }

    #[test]
    fn part2_example_2() {
        assert_eq!(test_support::do_part(part2, EXAMPLE_2), 8);
    }

    #[test]
    fn part2_example_3() {
        assert_eq!(test_support::do_part(part2, EXAMPLE_3), 4);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 483);
    }
}
