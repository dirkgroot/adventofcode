use crate::utils::Direction;

pub fn part1(input: &str) -> u64 {
    calculate_area(input.lines().map(Dig::parse1).collect::<Vec<_>>())
}

pub fn part2(input: &str) -> u64 {
    calculate_area(input.lines().map(Dig::parse2).collect::<Vec<_>>())
}

fn calculate_area(plan: Vec<Dig>) -> u64 {
    let plan = plan;
    let mut y = 0i64;
    let mut x = 0i64;
    let mut vertices = vec![(0i64, 0i64)];
    let mut path_length = 0u64;

    for p in plan {
        path_length += p.distance as u64;
        let (dy, dx) = match p.dir {
            Direction::N => (-p.distance, 0),
            Direction::E => (0, p.distance),
            Direction::S => (p.distance, 0),
            Direction::W => (0, -p.distance),
        };
        y += dy;
        x += dx;
        vertices.push((y, x));
    }

    area(&vertices) - (path_length / 2) + 1 + path_length
}

/// Calculate the area of a polygon using the Shoelace formula.
/// See https://en.wikipedia.org/wiki/Shoelace_formula.
fn area(path: &Vec<(i64, i64)>) -> u64 {
    let mut area = 0;
    let mut j = path.len() - 1;
    (0usize..path.len()).for_each(|i| {
        area += (path[j].1 + path[i].1) * (path[j].0 - path[i].0);
        j = i;
    });
    (area / 2).abs() as u64
}

struct Dig {
    dir: Direction,
    distance: i64,
}

impl Dig {
    fn parse1(line: &str) -> Self {
        let mut i = line.split(" ");
        let dir = match i.next() {
            Some("U") => Direction::N,
            Some("R") => Direction::E,
            Some("D") => Direction::S,
            Some("L") => Direction::W,
            _ => panic!(),
        };
        let distance = i.next().unwrap().parse().unwrap();
        Self { dir, distance }
    }

    fn parse2(line: &str) -> Self {
        let code = &line.split(" ").last().unwrap()[2..=7];
        let distance = i64::from_str_radix(&code[0..5], 16).unwrap();
        let dir = match code.chars().last() {
            Some('0') => Direction::E,
            Some('1') => Direction::S,
            Some('2') => Direction::W,
            Some('3') => Direction::N,
            _ => panic!(),
        };
        Self { dir, distance }
    }
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 18;

    const EXAMPLE: &str = "R 6 (#70c710)
D 5 (#0dc571)
L 2 (#5713f0)
D 2 (#d2c081)
R 2 (#59c680)
D 2 (#411b91)
L 5 (#8ceee2)
U 2 (#caa173)
L 1 (#1b58a2)
U 2 (#caa171)
R 2 (#7807d2)
U 3 (#a77fa3)
L 2 (#015232)
U 2 (#7a21e3)";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE), 62);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 39039);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE), 952408144115);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(
            test_support::do_part(part2, &read_input(DAY)),
            44644464596918
        );
    }
}
