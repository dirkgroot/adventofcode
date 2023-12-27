use crate::utils;

pub fn part1(input: &str, steps: u64) -> usize {
    let mut map = utils::parse_map(input);

    for _ in 0..steps {
        iterate(&mut map);
    }

    map.iter().flatten().filter(|c| **c == 'S').count()
}

fn part2(input: &str, i: usize) -> usize {
    let mut map = utils::parse_map(input);
    let map_height = map.len();

    map[map_height / 2][map_height / 2] = '.';

    let height = map.len() * 5;
    let width = height;
    let mut big_map = (0..height)
        .map(|y| {
            (0..width)
                .map(|x| map[y % map_height][x % map_height])
                .collect::<Vec<_>>()
        })
        .collect::<Vec<_>>();
    big_map[height / 2][width / 2] = 'S';

    let inner_diamond = os_after_iterations(&mut big_map, map_height / 2);
    let os2 = os_after_iterations(&mut big_map, map_height);
    let first_ring = os2 - inner_diamond;
    let os3 = os_after_iterations(&mut big_map, map_height);
    let second_ring = os3 - os2;

    let mut current_ring = first_ring;
    let add = second_ring - first_ring;
    let rings = i / map_height;
    let mut total = inner_diamond;
    for _ in 0..rings {
        total += current_ring;
        current_ring += add;
    }

    total
}

fn os_after_iterations(mut big_map: &mut Vec<Vec<char>>, iterations: usize) -> usize {
    for _ in 0..iterations {
        iterate(&mut big_map);
    }
    big_map.iter().flatten().filter(|c| **c == 'S').count()
}

fn iterate(map: &mut Vec<Vec<char>>) {
    let mut positions = Vec::new();
    (0..map.len()).for_each(|y| {
        (0..map[0].len()).for_each(|x| {
            if map[y][x] == 'S' {
                map[y][x] = '.';
                let n = neighbors(map, y, x);
                for n in n.iter().filter(|(y, x)| map[*y][*x] != '#') {
                    positions.push(*n);
                }
            }
        })
    });
    positions.iter().for_each(|(y, x)| map[*y][*x] = 'S')
}

fn neighbors(map: &Vec<Vec<char>>, y: usize, x: usize) -> Vec<(usize, usize)> {
    let mut result = Vec::new();
    if let Some(y) = y.checked_sub(1) {
        result.push((y, x));
    };
    if x < map[0].len() - 1 {
        result.push((y, x + 1));
    }
    if y < map.len() - 1 {
        result.push((y + 1, x));
    }
    if let Some(x) = x.checked_sub(1) {
        result.push((y, x));
    };
    result
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 21;

    const EXAMPLE: &str = "...........
.....###.#.
.###.##..#.
..#.#...#..
....#.#....
.##..S####.
.##..#...#.
.......##..
.##.#.####.
.##..##.##.
...........";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(|s| part1(s, 6), EXAMPLE), 16);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(
            test_support::do_part(|s| part1(s, 64), &read_input(DAY)),
            3724
        );
    }

    #[test]
    fn part2_solution() {
        assert_eq!(
            test_support::do_part(|input| part2(input, 26501365), &read_input(DAY)),
            620348631910321
        );
    }
}
