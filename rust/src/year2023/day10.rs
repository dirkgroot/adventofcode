use std::iter::successors;

pub fn part1(input: &str) -> usize {
    let main_loop = main_loop(&parse(input));
    main_loop.len() / 2
}

pub fn part2(input: &str) -> usize {
    let map = &parse(input);
    let main_loop = main_loop(&map);

    area(&main_loop) - (main_loop.len() / 2) + 1
}

fn area(vertices: &Vec<(usize, usize)>) -> usize {
    let mut area = 0i32;
    let mut j = vertices.len() - 1;
    (0usize..vertices.len()).for_each(|i| {
        area += (vertices[j].1 as i32 + vertices[i].1 as i32)
            * (vertices[j].0 as i32 - vertices[i].0 as i32);
        j = i;
    });
    (area / 2).abs() as usize
}

fn parse(input: &str) -> Vec<&str> {
    input.lines().collect::<Vec<_>>()
}

fn main_loop(map: &Vec<&str>) -> Vec<(usize, usize)> {
    let (start_y, start_x) = map
        .iter()
        .enumerate()
        .map(|(y, line)| (y, line.find("S")))
        .filter_map(|(y, x)| x.map(|x| (y, x)))
        .next()
        .unwrap();
    successors(Some(((0, 0), (start_y, start_x))), |((py, px), (y, x))| {
        let neighbors = neighbors(*y, *x, map.len(), map[0].len());
        let (new_y, new_x) = neighbors
            .iter()
            .find(|(ny, nx)| (ny != py || nx != px) && connected(*y, *x, *ny, *nx, map))
            .unwrap();
        if *new_y != start_y || *new_x != start_x {
            Some(((*y, *x), (*new_y, *new_x)))
        } else {
            None
        }
    })
    .map(|(_, cur)| cur)
    .collect()
}

fn neighbors(y: usize, x: usize, height: usize, width: usize) -> Vec<(usize, usize)> {
    let mut result = Vec::new();
    if y > 0 {
        result.push((y - 1, x));
    }
    if x < (width - 1) {
        result.push((y, x + 1));
    }
    if y < (height - 1) {
        result.push((y + 1, x));
    }
    if x > 0 {
        result.push((y, x - 1));
    }
    result
}

fn connected(y1: usize, x1: usize, y2: usize, x2: usize, map: &Vec<&str>) -> bool {
    let c1 = &map[y1][x1..=x1];
    let c2 = &map[y2][x2..=x2];
    let result = match c1 {
        "S" => match y2 {
            _ if y2 > y1 => "|LJ".contains(c2),
            _ if y2 < y1 => "|7F".contains(c2),
            _ if x2 > x1 => "-J7".contains(c2),
            _ if x2 < x1 => "-LF".contains(c2),
            _ => false,
        },
        "|" => match y2 {
            _ if y2 > y1 => "|LJS".contains(c2),
            _ if y2 < y1 => "|7FS".contains(c2),
            _ => false,
        },
        "-" => match y2 {
            _ if x2 > x1 => "-J7S".contains(c2),
            _ if x2 < x1 => "-LFS".contains(c2),
            _ => false,
        },
        "L" => match y2 {
            _ if y2 < y1 => "|7FS".contains(c2),
            _ if x2 > x1 => "-J7S".contains(c2),
            _ => false,
        },
        "J" => match y2 {
            _ if y2 < y1 => "|7FS".contains(c2),
            _ if x2 < x1 => "-LFS".contains(c2),
            _ => false,
        },
        "7" => match y2 {
            _ if y2 > y1 => "|LJS".contains(c2),
            _ if x2 < x1 => "-LFS".contains(c2),
            _ => false,
        },
        "F" => match y2 {
            _ if y2 > y1 => "|LJS".contains(c2),
            _ if x2 > x1 => "-J7S".contains(c2),
            _ => false,
        },
        _ => false,
    };
    result
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
