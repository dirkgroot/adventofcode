pub fn part1(input: &str) -> i32 {
    let (w1, w2) = parse(input);
    find_intersections(&w1, &w2)
        .map(|i| i.x.abs() + i.y.abs())
        .min()
        .unwrap()
}

pub fn part2(input: &str) -> i32 {
    let (w1, w2) = parse(input);
    find_intersections(&w1, &w2)
        .map(|c| steps_to(&w1, &c) + steps_to(&w2, &c))
        .min()
        .unwrap()
}

fn steps_to(w1: &Vec<Section>, c: &Coord2D) -> i32 {
    w1.iter()
        .scan((0, false), |(steps, stop), s| {
            if *stop {
                return None;
            } else if s.contains(c) {
                *steps += s.steps_to(c);
                *stop = true;
            } else {
                *steps += s.steps();
            }
            Some((*steps, *stop))
        })
        .last()
        .unwrap()
        .0
}

fn find_intersections<'a>(
    w1: &'a Vec<Section>,
    w2: &'a Vec<Section>,
) -> impl Iterator<Item=Coord2D> + 'a {
    let origin = Coord2D::new(0, 0);
    w1.iter()
        .flat_map(|s1| w2.iter().map(|s2| s1.intersects_at(s2)))
        .filter(|i| i.is_some())
        .map(|i| i.unwrap())
        .filter(move |i| *i != origin)
}

fn parse(input: &str) -> (Vec<Section>, Vec<Section>) {
    let wires = input.lines().collect::<Vec<&str>>();
    (parse_wire(wires[0]), parse_wire(wires[1]))
}

fn parse_wire(s: &str) -> Vec<Section> {
    let mut origin = Coord2D::new(0, 0);
    s.split(",")
        .map(|m| {
            let (s, org) = Section::parse(m, &origin);
            origin = org;
            s
        })
        .collect()
}

#[derive(PartialEq, Debug)]
struct Section {
    x1: i32,
    y1: i32,
    x2: i32,
    y2: i32,
    back: bool,
}

impl Section {
    fn new(x1: i32, y1: i32, x2: i32, y2: i32, back: bool) -> Section {
        Section {
            x1,
            y1,
            x2,
            y2,
            back,
        }
    }

    fn parse(s: &str, origin: &Coord2D) -> (Section, Coord2D) {
        let direction = s.chars().next().unwrap();
        let steps = s[1..].parse::<i32>().unwrap();
        match direction {
            'L' => (
                Section::new(origin.x - steps, origin.y, origin.x, origin.y, true),
                Coord2D::new(origin.x - steps, origin.y),
            ),
            'R' => (
                Section::new(origin.x, origin.y, origin.x + steps, origin.y, false),
                Coord2D::new(origin.x + steps, origin.y),
            ),
            'U' => (
                Section::new(origin.x, origin.y, origin.x, origin.y + steps, false),
                Coord2D::new(origin.x, origin.y + steps),
            ),
            'D' => (
                Section::new(origin.x, origin.y - steps, origin.x, origin.y, true),
                Coord2D::new(origin.x, origin.y - steps),
            ),
            _ => panic!("Invalid input!"),
        }
    }

    fn intersects_at(&self, o: &Section) -> Option<Coord2D> {
        if (self.x1..self.x2).contains(&o.x1) && (o.y1..o.y2).contains(&self.y1) {
            Some(Coord2D::new(o.x1, self.y1))
        } else if (o.x1..o.x2).contains(&self.x1) && (self.y1..self.y2).contains(&o.y1) {
            Some(Coord2D::new(self.x1, o.y1))
        } else {
            None
        }
    }

    fn steps(&self) -> i32 {
        if self.x1 != self.x2 {
            self.x2 - self.x1
        } else {
            self.y2 - self.y1
        }
    }

    fn steps_to(&self, c: &Coord2D) -> i32 {
        if self.x1 != self.x2 {
            if self.back {
                self.x2 - c.x
            } else {
                c.x - self.x1
            }
        } else if self.back {
            self.y2 - c.y
        } else {
            c.y - self.y1
        }
    }

    fn contains(&self, c: &Coord2D) -> bool {
        if self.x1 != self.x2 {
            c.y == self.y1 && (self.x1..=self.x2).contains(&c.x)
        } else {
            c.x == self.x1 && (self.y1..=self.y2).contains(&c.y)
        }
    }
}

#[derive(PartialEq, Debug)]
struct Coord2D {
    x: i32,
    y: i32,
}

impl Coord2D {
    fn new(x: i32, y: i32) -> Coord2D {
        Coord2D { x, y }
    }
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2019::read_input;

    use super::*;

    const DAY: i32 = 3;

    const EXAMPLE1: &str = "R8,U5,L5,D3\nU7,R6,D4,L4";

    #[test]
    fn parse_section() {
        assert_eq!(
            Section::parse("R10", &Coord2D::new(0, 0)),
            (Section::new(0, 0, 10, 0, false), Coord2D::new(10, 0))
        );
        assert_eq!(
            Section::parse("L10", &Coord2D::new(0, 0)),
            (Section::new(-10, 0, 0, 0, true), Coord2D::new(-10, 0))
        );
        assert_eq!(
            Section::parse("U10", &Coord2D::new(0, 0)),
            (Section::new(0, 0, 0, 10, false), Coord2D::new(0, 10))
        );
        assert_eq!(
            Section::parse("D10", &Coord2D::new(0, 0)),
            (Section::new(0, -10, 0, 0, true), Coord2D::new(0, -10))
        );
    }

    #[test]
    fn intersection() {
        let s1 = Section::new(0, 0, 10, 0, false);
        let s2 = Section::new(5, -5, 5, 5, false);
        let s3 = Section::new(11, 0, 15, 0, false);
        assert_eq!(s1.intersects_at(&s2), Some(Coord2D::new(5, 0)));
        assert_eq!(s2.intersects_at(&s1), Some(Coord2D::new(5, 0)));
        assert_eq!(s1.intersects_at(&s3), None);
    }

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE1), 6);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE1), 30);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 855);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 11238);
    }
}
