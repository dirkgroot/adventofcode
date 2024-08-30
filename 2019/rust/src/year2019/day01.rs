pub fn part1(input: &str) -> i32 {
    parse(input).map(mass_fuel_requirement).sum()
}

pub fn part2(input: &str) -> i32 {
    parse(input)
        .map(mass_fuel_requirement)
        .map(|fuel| fuel + fuel_fuel_requirement(fuel))
        .sum()
}

fn parse(input: &str) -> impl Iterator<Item = i32> + '_ {
    input.lines().map(|line| line.parse::<i32>().unwrap())
}

fn fuel_fuel_requirement(fuel: i32) -> i32 {
    let fuel_fuel = mass_fuel_requirement(fuel);
    match fuel_fuel {
        ..=0 => 0,
        _ => fuel_fuel + fuel_fuel_requirement(fuel_fuel),
    }
}

fn mass_fuel_requirement(mass: i32) -> i32 {
    mass / 3 - 2
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2019::read_input;

    use super::*;

    const DAY: i32 = 1;

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 3406527);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 5106932);
    }
}
