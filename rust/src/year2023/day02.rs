use std::collections::HashMap;

pub fn part1(input: &str) -> i32 {
    let games = parse(input);

    games
        .iter()
        .filter(|g| {
            g.sets.iter().all(|s| {
                (s.r == 0 || s.r <= 12) && (s.g == 0 || s.g <= 13) && (s.b == 0 || s.b <= 14)
            })
        })
        .map(|g| g.id)
        .sum()
}

pub fn part2(input: &str) -> i32 {
    let games = parse(input);

    games
        .iter()
        .map(|game| {
            let r = game.sets.iter().map(|s| s.r).max().unwrap();
            let g = game.sets.iter().map(|s| s.g).max().unwrap();
            let b = game.sets.iter().map(|s| s.b).max().unwrap();

            r * g * b
        })
        .sum()
}

fn parse(input: &str) -> Vec<Game> {
    input.lines().map(|l| Game::parse(l)).collect()
}

struct Game {
    id: i32,
    sets: Vec<CubeSet>,
}

impl Game {
    fn new(id: i32, sets: Vec<CubeSet>) -> Self {
        Self { id, sets }
    }

    fn parse(input: &str) -> Self {
        let game = input.split(": ").collect::<Vec<_>>();
        let id = game[0][5..].parse::<i32>().unwrap();
        let set_strings = game[1].split("; ");
        let sets = set_strings
            .map(|set_string| CubeSet::parse(set_string))
            .collect::<_>();

        Game::new(id, sets)
    }
}

struct CubeSet {
    r: i32,
    g: i32,
    b: i32,
}

impl CubeSet {
    pub fn new(r: i32, g: i32, b: i32) -> Self {
        Self { r, g, b }
    }

    fn parse(input: &str) -> Self {
        let cubes = input
            .split(", ")
            .map(|cube| {
                let colors = cube.split(" ").collect::<Vec<_>>();
                (colors[1], colors[0].parse::<i32>().unwrap())
            })
            .collect::<HashMap<_, _>>();
        let find_color = |c| *cubes.get(c).unwrap_or(&0);

        CubeSet::new(find_color("red"), find_color("green"), find_color("blue"))
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
