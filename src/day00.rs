pub fn part1(input: &str) -> i32 {
    parse(input)
}

pub fn part2(input: &str) -> i32 {
    parse(input) + 1
}

fn parse(input: &str) -> i32 {
    match input.parse::<i32>() {
        Ok(i) => i,
        Err(_) => -1
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use crate::support;

    #[test]
    fn part1_solution() {
        let result = support::do_part(part1, INPUT);
        assert_eq!(result, 0);
    }

    #[test]
    fn part2_solution() {
        let result = support::do_part(part2, INPUT);
        assert_eq!(result, 1);
    }

    const INPUT: &str = "0";
}
