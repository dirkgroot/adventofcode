use std::cmp::Ordering;
use std::collections::HashMap;

pub fn part1(input: &str) -> i32 {
    let mut decks = parse(input);
    decks.sort_by(|(deck1, _), (deck2, _)| compare_decks(deck1, deck2, false));
    total_winnings(&decks)
}

pub fn part2(input: &str) -> i32 {
    let mut decks = parse(input);
    decks.sort_by(|(deck1, _), (deck2, _)| compare_decks(deck1, deck2, true));
    total_winnings(&decks)
}

fn parse(input: &str) -> Vec<(&str, i32)> {
    input
        .lines()
        .map(|l| l.split_once(" ").unwrap())
        .map(|(deck, bid)| (deck, bid.parse::<i32>().unwrap()))
        .collect::<Vec<_>>()
}

fn compare_decks(d1: &str, d2: &str, joker: bool) -> Ordering {
    let result = deck_type(d1, joker).cmp(&deck_type(d2, joker));
    if result == Ordering::Equal {
        compare_by_cards(d1, d2, joker)
    } else {
        result
    }
}

fn deck_type(deck: &str, joker: bool) -> i32 {
    let mut count: HashMap<char, i32> = HashMap::new();
    deck.chars().for_each(|c| {
        let option = count.get(&c);
        count.insert(c, option.map_or(1, |n| *n + 1));
    });
    let groups = count.len();
    let values = count.values().map(|v| *v).collect::<Vec<i32>>();
    if groups == 1 {
        return 7;
    }
    if joker && count.contains_key(&'J') {
        let jokers = count.remove(&'J').unwrap();
        let groups = count.len();
        let values = count.values().map(|v| *v).collect::<Vec<i32>>();
        return match groups {
            1 => 7,
            2 if values.iter().any(|v| *v + jokers == 4) => 6,
            2 => 5,
            3 if values.iter().any(|v| *v + jokers == 3) => 4,
            3 => 3,
            _ => 2,
        };
    }
    match groups {
        2 if values[0] == 1 || values[0] == 4 => 6,
        2 => 5,
        3 if values.iter().any(|v| *v == 3) => 4,
        3 => 3,
        4 => 2,
        _ => 1,
    }
}

fn compare_by_cards(d1: &str, d2: &str, joker: bool) -> Ordering {
    d1.chars()
        .zip(d2.chars())
        .map(|(c1, c2)| card_value(&c1, joker).cmp(&card_value(&c2, joker)))
        .find(|o| *o != Ordering::Equal)
        .unwrap_or(Ordering::Equal)
}

fn card_value(card: &char, joker: bool) -> i32 {
    match card {
        '2' if joker => 1,
        '2' => 0,
        '3' if joker => 2,
        '3' => 1,
        '4' if joker => 3,
        '4' => 2,
        '5' if joker => 4,
        '5' => 3,
        '6' if joker => 5,
        '6' => 4,
        '7' if joker => 6,
        '7' => 5,
        '8' if joker => 7,
        '8' => 6,
        '9' if joker => 8,
        '9' => 7,
        'T' if joker => 9,
        'T' => 8,
        'J' if joker => 0,
        'J' => 9,
        'Q' => 10,
        'K' => 11,
        'A' => 12,
        _ => panic!(),
    }
}

fn total_winnings(decks: &Vec<(&str, i32)>) -> i32 {
    decks
        .iter()
        .enumerate()
        .map(|(i, (_, bid))| *bid * (i as i32 + 1))
        .sum()
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 7;

    const EXAMPLE: &str = "32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE), 6440);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 252656917);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE), 5905);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 253499763);
    }
}
