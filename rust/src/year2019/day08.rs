const WIDTH: usize = 25;
const HEIGHT: usize = 6;

pub fn part1(input: &str) -> i32 {
    parse(input)
        .iter()
        .map(|layer| (layer, layer.iter().filter(|i| **i == 0).count()))
        .min_by(|(_, zeroes1), (_, zeroes2)| zeroes1.cmp(&zeroes2))
        .map_or(-1, |(layer, _)| {
            let ones = layer.iter().filter(|i| **i == 1).count();
            let twos = layer.iter().filter(|i| **i == 2).count();
            (ones * twos) as i32
        })
}

pub fn part2(input: &str) -> String {
    let layers = parse(input);
    let mut image = vec![2u32; layers[0].len()];

    layers.iter().for_each(|layer| {
        layer.iter().enumerate().for_each(|(i, v)| {
            if image[i] == 2 && *v != 2u32 {
                image[i] = *v;
            }
        })
    });

    let mut result = String::from("\n");
    image.chunks(WIDTH).for_each(|row| {
        row.iter().for_each(|i| match i {
            0 => result.push('.'),
            1 => result.push('*'),
            2 => result.push('.'),
            _ => panic!(),
        });
        result.push('\n');
    });

    result
}

fn parse(input: &str) -> Vec<Vec<u32>> {
    input
        .chars()
        .map(|c| c.to_digit(10).unwrap())
        .collect::<Vec<_>>()
        .chunks(WIDTH * HEIGHT)
        .map(|c| Vec::from(c))
        .collect::<Vec<_>>()
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2019::read_input;

    use super::*;

    const DAY: i32 = 8;

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 1820);
    }

    const SOLUTION: &str = "
****.*..*.*..*..**....**.
...*.*..*.*.*..*..*....*.
..*..*..*.**...*.......*.
.*...*..*.*.*..*.......*.
*....*..*.*.*..*..*.*..*.
****..**..*..*..**...**..
";

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), SOLUTION);
    }
}
