pub fn part1(input: &str) -> i32 {
    input.split(",").map(|step| hash(step)).sum()
}

pub fn part2(input: &str) -> usize {
    let boxes = perform_steps(input);
    focusing_power(&boxes)
}

fn perform_steps(input: &str) -> Vec<Vec<(String, usize)>> {
    let mut boxes: Vec<Vec<(String, usize)>> = vec![Vec::new(); 256];
    input.split(",").for_each(|step| {
        if let Some((label, focal_length)) = step.split_once("=") {
            let i = hash(&label) as usize;
            let focal_length = focal_length.parse().unwrap();
            if let Some(idx) = boxes[i].iter().position(|(l, _)| *l == label) {
                boxes[i][idx] = (String::from(label), focal_length);
            } else {
                boxes[i].push((String::from(label), focal_length));
            }
        } else {
            let label = &step[..step.len() - 1];
            let i = hash(&label) as usize;
            if let Some(idx) = boxes[i].iter().position(|(l, _)| *l == label) {
                boxes[i].remove(idx);
            }
        }
    });
    boxes
}

fn hash(input: &str) -> i32 {
    let mut hash = 0i32;
    let bytes = input.as_bytes();
    for i in 0..bytes.len() {
        hash += bytes[i] as i32;
        hash *= 17;
        hash %= 256;
    }
    hash
}

fn focusing_power(boxes: &Vec<Vec<(String, usize)>>) -> usize {
    (0..boxes.len())
        .flat_map(|i| (0..boxes[i].len()).map(move |j| (i + 1) * (j + 1) * boxes[i][j].1))
        .sum()
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 15;

    const EXAMPLE: &str = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE), 1320);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 522547);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE), 145);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 229271);
    }
}
