use std::collections::HashMap;
use std::ops::Range;

use crate::year2023::day05::Type::{
    Fertilizer, Humidity, Light, Location, Seed, Soil, Temperature, Water,
};

pub fn part1(input: &str) -> i64 {
    let almanac = Almanac::parse(input);
    almanac
        .seeds
        .iter()
        .filter_map(|seed| almanac.seed_location(*seed))
        .min()
        .unwrap()
}

pub fn part2(input: &str) -> i64 {
    let almanac = Almanac::parse(input);
    let seed_ranges = almanac
        .seeds
        .chunks(2)
        .map(|c| c[0]..(c[0] + c[1]))
        .collect::<Vec<_>>();
    almanac
        .location_ranges(&seed_ranges)
        .iter()
        .map(|r| r.start)
        .min()
        .unwrap()
}

struct Almanac {
    seeds: Vec<i64>,
    maps: HashMap<Type, Map>,
}

impl Almanac {
    fn parse(input: &str) -> Self {
        let mut sections = input.split("\n\n");
        let seeds = Self::parse_seeds(sections.next().unwrap());
        let seed_to_soil = Self::parse_map(sections.next().unwrap(), Soil);
        let soil_to_fertilizer = Self::parse_map(sections.next().unwrap(), Fertilizer);
        let fertilizer_to_water = Self::parse_map(sections.next().unwrap(), Water);
        let water_to_light = Self::parse_map(sections.next().unwrap(), Light);
        let light_to_temperature = Self::parse_map(sections.next().unwrap(), Temperature);
        let temperature_to_humidity = Self::parse_map(sections.next().unwrap(), Humidity);
        let humidity_to_location = Self::parse_map(sections.next().unwrap(), Location);

        Self {
            seeds,
            maps: HashMap::from([
                (Seed, seed_to_soil),
                (Soil, soil_to_fertilizer),
                (Fertilizer, fertilizer_to_water),
                (Water, water_to_light),
                (Light, light_to_temperature),
                (Temperature, temperature_to_humidity),
                (Humidity, humidity_to_location),
            ]),
        }
    }

    fn parse_seeds(seeds: &str) -> Vec<i64> {
        seeds
            .split_whitespace()
            .skip(1)
            .map(|s| s.parse::<i64>().unwrap())
            .collect()
    }

    fn parse_map(input: &str, destination: Type) -> Map {
        let mut ranges = input
            .lines()
            .skip(1)
            .map(|line| {
                let mut split = line.split_whitespace();
                let (dest, src, size) = (
                    split.next().unwrap().parse::<i64>().unwrap(),
                    split.next().unwrap().parse::<i64>().unwrap(),
                    split.next().unwrap().parse::<i64>().unwrap(),
                );
                (src..(src + size), dest..(dest + size))
            })
            .collect::<Vec<(Range<i64>, Range<i64>)>>();
        ranges.sort_by(|a, b| a.0.start.cmp(&b.0.start));
        Map {
            destination,
            ranges,
        }
    }

    fn seed_location(&self, seed: i64) -> Option<i64> {
        self.find_location(&self.maps[&Seed], seed)
    }

    fn find_location(&self, map: &Map, item: i64) -> Option<i64> {
        let ranges = map.ranges.iter().find(|(src, _)| src.contains(&item));

        let dest_item = ranges.map_or(item, |(src, dest)| {
            let index = item - src.start;
            dest.start + index
        });

        if map.destination == Location {
            Some(dest_item)
        } else {
            let destination = map.destination;
            self.find_location(&self.maps[&destination], dest_item)
        }
    }

    fn location_ranges(&self, seed_ranges: &Vec<Range<i64>>) -> Vec<Range<i64>> {
        self.translate_ranges(&self.maps[&Seed], seed_ranges)
    }

    fn translate_ranges(&self, map: &Map, src_ranges: &Vec<Range<i64>>) -> Vec<Range<i64>> {
        let result = src_ranges
            .iter()
            .flat_map(|src| map.translate_range(src))
            .collect::<Vec<_>>();
        if map.destination != Location {
            self.translate_ranges(&self.maps[&map.destination], &result)
        } else {
            result
        }
    }
}

#[derive(Eq, PartialEq, Hash, Copy, Clone, Debug)]
enum Type {
    Seed,
    Soil,
    Fertilizer,
    Water,
    Light,
    Temperature,
    Humidity,
    Location,
}

struct Map {
    destination: Type,
    ranges: Vec<(Range<i64>, Range<i64>)>,
}

impl Map {
    fn translate_range(&self, src: &Range<i64>) -> Vec<Range<i64>> {
        self.ranges
            .iter()
            .find(|(map_src, _)| src.start < map_src.end && src.end > map_src.start)
            .map_or(vec![src.start..src.end], |(map_src, _)| {
                let mut points = vec![src.start, src.end, map_src.start, map_src.end];
                points.sort();
                points
                    .windows(2)
                    .filter(|ps| ps[0] >= src.start && ps[1] <= src.end)
                    .map(|ps| ps[0]..ps[1])
                    .map(|r| self.translate(&r))
                    .collect::<Vec<_>>()
            })
    }

    fn translate(&self, range: &Range<i64>) -> Range<i64> {
        self.ranges
            .iter()
            .find(|(s, _)| s.contains(&range.start))
            .map_or(range.start..range.end, |(s, d)| {
                let offset = d.start - s.start;
                (range.start + offset)..(range.end + offset)
            })
    }
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 5;

    const EXAMPLE: &str = "seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE), 35);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 218513636);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE), 46);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 81956384);
    }
}
