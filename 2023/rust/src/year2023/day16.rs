use crate::utils;
use crate::utils::Direction;
use std::cmp::max;

pub fn part1(input: &str) -> usize {
    let map = utils::parse_map(input);
    analyze(&map, 0usize, 0usize, Direction::E)
}

pub fn part2(input: &str) -> Option<usize> {
    let map = utils::parse_map(input);
    let max_h = (0..map.len()).map(|y| {
        max(
            analyze(&map, y, 0, Direction::E),
            analyze(&map, y, map[0].len() - 1, Direction::W),
        )
    });
    let max_v = (0..map[0].len()).map(|x| {
        max(
            analyze(&map, 0, x, Direction::S),
            analyze(&map, map.len() - 1, x, Direction::N),
        )
    });
    Some(max(max_h.max()?, max_v.max()?))
}

fn analyze(map: &Vec<Vec<char>>, start_y: usize, start_x: usize, start_dir: Direction) -> usize {
    let mut trace_map = vec![vec![Trace::new(); map[0].len()]; map.len()];
    trace(&map, &mut trace_map, start_y, start_x, start_dir);
    trace_map.iter().flatten().filter(|c| c.energized()).count()
}

fn trace(
    map: &Vec<Vec<char>>,
    trace_map: &mut Vec<Vec<Trace>>,
    mut y: usize,
    mut x: usize,
    mut dir: Direction,
) {
    loop {
        if trace_map[y][x].mark(dir) {
            return;
        }
        match map[y][x] {
            '/' => match dir {
                Direction::N => dir = Direction::E,
                Direction::E => dir = Direction::N,
                Direction::S => dir = Direction::W,
                Direction::W => dir = Direction::S,
            },
            '\\' => match dir {
                Direction::N => dir = Direction::W,
                Direction::E => dir = Direction::S,
                Direction::S => dir = Direction::E,
                Direction::W => dir = Direction::N,
            },
            '|' => match dir {
                Direction::E | Direction::W => {
                    dir = Direction::N;
                    trace(map, trace_map, y, x, Direction::S);
                }
                _ => {}
            },
            '-' => match dir {
                Direction::N | Direction::S => {
                    dir = Direction::W;
                    trace(map, trace_map, y, x, Direction::E);
                }
                _ => {}
            },
            _ => {}
        }
        match dir {
            Direction::N => {
                if y == 0 {
                    return;
                }
                y -= 1;
            }
            Direction::E => {
                if x == map[0].len() - 1 {
                    return;
                }
                x += 1;
            }
            Direction::S => {
                if y == map.len() - 1 {
                    return;
                }
                y += 1;
            }
            Direction::W => {
                if x == 0 {
                    return;
                }
                x -= 1;
            }
        }
    }
}

#[derive(Clone)]
struct Trace {
    n: bool,
    e: bool,
    s: bool,
    w: bool,
}

impl Trace {
    pub fn new() -> Self {
        Self {
            n: false,
            e: false,
            s: false,
            w: false,
        }
    }

    fn energized(&self) -> bool {
        self.n || self.e || self.s || self.w
    }

    fn mark(&mut self, dir: Direction) -> bool {
        let visited = match dir {
            Direction::N => &mut self.n,
            Direction::E => &mut self.e,
            Direction::S => &mut self.s,
            Direction::W => &mut self.w,
        };
        if *visited {
            return true;
        }
        *visited = true;
        false
    }
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 16;

    const EXAMPLE: &str = r".|...\....
|.-.\.....
.....|-...
........|.
..........
.........\
..../.\\..
.-.-/..|..
.|....-|.\
..//.|....";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE), 46);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 6816);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE), Some(51));
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), Some(8163));
    }
}
