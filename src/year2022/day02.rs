use crate::year2022::day02::Move::{Paper, Rock, Scissors};
use crate::year2022::day02::Outcome::{Draw, Lose, Win};

pub fn part1(input: &str) -> i32 {
    round_strings(input)
        .map(|round| parse_round(round, Move::parse))
        .fold(0, |acc, (other, me)| {
            acc + me as i32
                + match (other, me) {
                    (Rock, Paper) | (Paper, Scissors) | (Scissors, Rock) => Win,
                    (Rock, Rock) | (Paper, Paper) | (Scissors, Scissors) => Draw,
                    (Rock, Scissors) | (Paper, Rock) | (Scissors, Paper) => Lose,
                } as i32
        })
}

pub fn part2(input: &str) -> i32 {
    round_strings(input)
        .map(|round| parse_round(round, Outcome::parse))
        .fold(0, |acc, (other, outcome)| {
            acc + outcome as i32
                + match (other, outcome) {
                    (Rock, Draw) | (Paper, Lose) | (Scissors, Win) => Rock,
                    (Rock, Win) | (Paper, Draw) | (Scissors, Lose) => Paper,
                    (Rock, Lose) | (Paper, Win) | (Scissors, Draw) => Scissors,
                } as i32
        })
}

fn round_strings(input: &str) -> impl Iterator<Item = (&str, &str)> {
    input.lines().map(|line| line.split_once(" ").unwrap())
}

fn parse_round<T>(round: (&str, &str), parser: fn(&str) -> T) -> (Move, T) {
    (Move::parse(round.0), parser(round.1))
}

#[derive(Copy, Clone)]
enum Move {
    Rock = 1,
    Paper = 2,
    Scissors = 3,
}

impl Move {
    fn parse(char: &str) -> Move {
        match char {
            "A" | "X" => Rock,
            "B" | "Y" => Paper,
            "C" | "Z" => Scissors,
            _ => panic!("Invalid input!"),
        }
    }
}

#[derive(Copy, Clone)]
enum Outcome {
    Win = 6,
    Draw = 3,
    Lose = 0,
}

impl Outcome {
    fn parse(s: &str) -> Outcome {
        match s {
            "X" => Lose,
            "Y" => Draw,
            "Z" => Win,
            _ => panic!("Invalid input!"),
        }
    }
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2022::read_input;

    use super::*;

    const DAY: i32 = 2;

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 13484);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 13433);
    }
}
