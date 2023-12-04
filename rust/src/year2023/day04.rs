use std::collections::{HashMap, HashSet};

pub fn part1(input: &str) -> i32 {
    parse(input).iter().fold(0, |acc, (_, c)| acc + c.worth())
}

pub fn part2(input: &str) -> i32 {
    let cards = parse(input);
    cards.iter().map(|(_, c)| 1 + cards_won(c, &cards)).sum()
}

fn cards_won(card: &Card, all_cards: &HashMap<i32, Card>) -> i32 {
    let won_cards = (card.id + 1)..=(card.id + card.matching_numbers);
    card.matching_numbers + won_cards.fold(0, |acc, id| acc + cards_won(&all_cards[&id], all_cards))
}

fn parse(input: &str) -> HashMap<i32, Card> {
    input
        .lines()
        .map(|line| {
            let (card, rest) = line.split_once(": ").unwrap();
            let id = card[5..].trim().parse::<i32>().unwrap();
            let (my_numbers, winning_numbers) = rest.trim().split_once(" | ").unwrap();
            let my_numbers = parse_number_list(my_numbers).collect::<Vec<_>>();
            let winning_numbers = parse_number_list(winning_numbers).collect::<HashSet<_>>();

            (id, Card::new(id, my_numbers, winning_numbers))
        })
        .collect()
}

fn parse_number_list(winning_numbers: &str) -> impl Iterator<Item = i32> + '_ {
    winning_numbers
        .split(" ")
        .filter(|n| n.trim().len() > 0)
        .map(|n| n.trim().parse::<i32>().unwrap())
}

struct Card {
    id: i32,
    matching_numbers: i32,
}

impl Card {
    fn new(id: i32, my_numbers: Vec<i32>, winning_numbers: HashSet<i32>) -> Self {
        let matching_numbers = my_numbers
            .iter()
            .filter(|n| winning_numbers.contains(*n))
            .count();
        Self {
            id,
            matching_numbers: matching_numbers as i32,
        }
    }

    fn worth(&self) -> i32 {
        let matching = self.matching_numbers;
        if matching == 0 {
            0
        } else {
            2i32.pow(matching as u32 - 1)
        }
    }
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 4;

    const EXAMPLE: &str = "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE), 13);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 26426);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE), 30);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 6227972);
    }
}
