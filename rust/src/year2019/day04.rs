use std::iter::successors;

pub fn part1(input: &str) -> i32 {
    count_valid_between(input, is_valid_1)
}

pub fn part2(input: &str) -> i32 {
    count_valid_between(input, is_valid_2)
}

fn is_valid_1(i: i32) -> bool {
    let (has_double, decreases) =
        digit_pairs(i).fold((false, true), |(has_double, decreases), (prev, next)| {
            (has_double || next == prev, decreases && next <= prev)
        });
    has_double && decreases
}

fn is_valid_2(i: i32) -> bool {
    let (has_req_group, _, decreases) = digit_pairs(i).fold(
        (false, 1, true),
        |(has_req_group, cur_group, decreases), (prev, next)| {
            let (cur_group, has_req_group) = if next == prev {
                (cur_group + 1, has_req_group)
            } else {
                (1, has_req_group || cur_group == 2)
            };
            (has_req_group, cur_group, decreases && next <= prev)
        },
    );
    has_req_group && decreases
}

fn digit_pairs(i: i32) -> impl Iterator<Item = (i32, i32)> {
    successors(Some((i, i / 10)), |(_, b)| match *b {
        n if n > 0 => Some((*b, n / 10)),
        _ => None,
    })
    .map(|(a, b)| (a % 10, b % 10))
}

fn count_valid_between(input: &str, is_valid: fn(i32) -> bool) -> i32 {
    let (min, max) = parse(input);
    successors(Some(next_valid(min, is_valid)), |i| {
        match next_valid(*i, is_valid) {
            n if n <= max => Some(n),
            _ => None,
        }
    })
    .count() as i32
}

fn parse(input: &str) -> (i32, i32) {
    let s = input.split("-").collect::<Vec<&str>>();
    (s[0].parse::<i32>().unwrap(), s[1].parse::<i32>().unwrap())
}

fn next_valid(i: i32, is_valid: fn(i32) -> bool) -> i32 {
    successors(Some(next(i)), |s| Some(next(*s)))
        .find(|n| is_valid(*n))
        .unwrap()
}

fn next(i: i32) -> i32 {
    if i % 10 == 9 {
        let n = next(i / 10);
        (n * 10) + n % 10
    } else {
        i + 1
    }
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2019::read_input;

    use super::*;

    const DAY: i32 = 4;

    #[test]
    fn next_number() {
        assert_eq!(123466, next(123459));
        assert_eq!(123555, next(123549));
    }

    #[test]
    fn next_valid_number() {
        assert_eq!(266666, next_valid(265275, is_valid_1));
    }

    #[test]
    fn validation() {
        assert_eq!(false, is_valid_1(265275));
        assert_eq!(false, is_valid_1(123456));
        assert_eq!(true, is_valid_1(234577));
        assert_eq!(true, is_valid_2(112233));
        assert_eq!(false, is_valid_2(123444));
        assert_eq!(true, is_valid_2(111122));
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 960);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 626);
    }
}
