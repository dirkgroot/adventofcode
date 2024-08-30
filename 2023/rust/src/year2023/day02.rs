use std::collections::HashMap;

pub fn part1(input: &str) -> i32 {
    parse(input)
        .filter(|g| g.sets.iter().all(|s| s.r <= 12 && s.g <= 13 && s.b <= 14))
        .fold(0, |acc, g| acc + g.id)
}

pub fn part2(input: &str) -> i32 {
    parse(input).fold(0, |acc, g| {
        let red = g.sets.iter().map(|s| s.r).max().unwrap();
        let green = g.sets.iter().map(|s| s.g).max().unwrap();
        let blue = g.sets.iter().map(|s| s.b).max().unwrap();

        acc + red * green * blue
    })
}

fn parse(input: &str) -> impl Iterator<Item = Game> + '_ {
    input.lines().map(Game::parse)
}

struct Game {
    id: i32,
    sets: Vec<CubeSet>,
}

impl Game {
    fn parse(input: &str) -> Self {
        let (id, sets) = input.split_once(": ").unwrap();
        let id = id[5..].parse::<i32>().unwrap();
        let sets = sets.split("; ").map(CubeSet::parse).collect();

        Self { id, sets }
    }
}

struct CubeSet {
    r: i32,
    g: i32,
    b: i32,
}

impl CubeSet {
    fn parse(input: &str) -> Self {
        let cubes = input
            .split(", ")
            .map(|cube| {
                let (cubes, color) = cube.split_once(" ").unwrap();
                (color, cubes.parse::<i32>().unwrap())
            })
            .collect::<HashMap<_, _>>();
        let find_color = |c| *cubes.get(c).unwrap_or(&0);

        Self {
            r: find_color("red"),
            g: find_color("green"),
            b: find_color("blue"),
        }
    }
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 2;

    const EXAMPLE: &str = "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE), 8);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 2331);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE), 2286);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 71585);
    }
}
