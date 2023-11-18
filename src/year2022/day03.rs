pub fn part1(input: &str) -> i32 {
    input
        .lines()
        .map(|sack| sack.split_at(sack.len() / 2))
        .map(|(c1, c2)| c1.chars().find(|item| c2.contains(*item)).unwrap())
        .fold(0, |acc, item| acc + item_priority(item))
}

pub fn part2(input: &str) -> i32 {
    input
        .lines()
        .collect::<Vec<&str>>()
        .chunks(3)
        .map(|group| {
            group[0]
                .chars()
                .find(|c| group[1..].iter().all(|s| s.contains(*c)))
                .unwrap()
        })
        .fold(0, |acc, item| acc + item_priority(item))
}

fn item_priority(c: char) -> i32 {
    match c {
        'a'..='z' => c as i32 - 'a' as i32 + 1,
        'A'..='Z' => c as i32 - 'A' as i32 + 27,
        _ => panic!(),
    }
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2022::read_input;

    use super::*;

    const DAY: i32 = 3;

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 7903);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 2548);
    }
}
