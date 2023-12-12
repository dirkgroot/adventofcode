use std::collections::HashMap;
use std::iter::repeat;

pub fn part1(input: &str) -> usize {
    total_arrangements(input, 1)
}

pub fn part2(input: &str) -> usize {
    total_arrangements(input, 5)
}

fn total_arrangements(input: &str, unfold: usize) -> usize {
    input
        .lines()
        .map(|l| Record::parse(l, unfold))
        .map(|record| record.arrangements())
        .sum()
}

struct Record {
    springs: Vec<char>,
    groups: Vec<usize>,
}

impl Record {
    fn parse(line: &str, unfold: usize) -> Self {
        let (springs, groups) = line.split_once(" ").unwrap();
        let springs = repeat(springs).take(unfold).collect::<Vec<_>>().join("?");
        let springs = springs.chars().collect();
        let groups = repeat(groups).take(unfold).collect::<Vec<_>>().join(",");
        let groups = groups.split(",").map(|g| g.parse().unwrap()).collect();
        Self { springs, groups }
    }

    fn arrangements(&self) -> usize {
        let mut cache: HashMap<(usize, usize), usize> = HashMap::new();
        self.count_arrangements(&mut cache, 0, 0)
    }

    fn count_arrangements(
        &self,
        cache: &mut HashMap<(usize, usize), usize>,
        group_idx: usize,
        pos: usize,
    ) -> usize {
        if let Some(r) = cache.get(&(group_idx, pos)) {
            return *r;
        }

        let group_len = self.groups[group_idx];
        let max_pos = self.springs.len() - group_len;

        let positions = (pos..=max_pos)
            .take_while(|pos2| pos2.checked_sub(1).map(|i| self.springs[i]) != Some('#'))
            .filter(|pos2| {
                let rdelimiter = self.springs.get(pos2 + group_len);
                let has_valid_delimiter = rdelimiter != Some(&'#');
                let is_group_candidate = self.springs[*pos2..pos2 + group_len]
                    .iter()
                    .all(|c| *c != '.');
                has_valid_delimiter && is_group_candidate
            });

        let result = if group_idx == self.groups.len() - 1 {
            positions
                .filter(|pos2| self.springs[pos2 + group_len..].iter().all(|c| *c != '#'))
                .count()
        } else {
            positions
                .map(|p| self.count_arrangements(cache, group_idx + 1, p + group_len + 1))
                .sum()
        };
        *cache.entry((group_idx, pos)).or_insert(0) += result;
        result
    }
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 12;

    const EXAMPLE: &str = "???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE), 21);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 7460);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE), 525152);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(
            test_support::do_part(part2, &read_input(DAY)),
            6720660274964
        );
    }
}
