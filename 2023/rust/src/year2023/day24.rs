use rust_decimal::prelude::{FromPrimitive, One, ToPrimitive, Zero};
use rust_decimal::Decimal;

pub fn part1(input: &str, y1: f64, x1: f64, y2: f64, x2: f64) -> usize {
    let hail = parse(input);

    let y1 = Decimal::from_f64(y1).unwrap();
    let x1 = Decimal::from_f64(x1).unwrap();
    let y2 = Decimal::from_f64(y2).unwrap();
    let x2 = Decimal::from_f64(x2).unwrap();
    hail.iter()
        .enumerate()
        .flat_map(|(a, hs1)| {
            hail.iter()
                .skip(a + 1)
                .filter_map(|hs2| hs1.intersection(hs2))
        })
        .filter(|i| i.y >= y1 && i.y <= y2 && i.x >= x1 && i.x <= x2)
        .count()
}

pub fn part2(input: &str) -> usize {
    let hail = parse(input);

    let mut n = 0;
    loop {
        for x in 0..n + 1 {
            let y = n - x;
            for adjust_x in [-1, 1] {
                for adjust_y in [-1, 1] {
                    let ax = x * adjust_x;
                    let ay = y * adjust_y;

                    if ax == -193 && ay == -230 {
                        println!("This is the solution!");
                    }
                    let ax: Decimal = ax.into();
                    let ay: Decimal = ay.into();

                    let adjusted = hail
                        .iter()
                        .map(|h| Hailstone {
                            velocity: Point3D::new(
                                h.velocity.x - ax,
                                h.velocity.y - ay,
                                h.velocity.z,
                            ),
                            pos: h.pos,
                        })
                        .collect::<Vec<_>>();

                    let h1 = &adjusted[0];
                    let mut intersection = None;
                    let mut p = None;
                    for h2 in adjusted[1..].iter() {
                        p = h2.intersection(h1);
                        match p {
                            None => break,
                            _ if intersection.is_none() => intersection = p,
                            _ if p != intersection => break,
                            _ => {}
                        }
                    }

                    if p != intersection {
                        continue;
                    }
                    if let Some(i) = intersection {
                        let h = &adjusted[0];
                        let t = h.get_t(i.y, i.x);
                        let z = h.pos.z + t * (h.velocity.z - i.z);

                        return (i.x + i.y + z).to_usize().unwrap();
                    }
                }
            }
        }
        n += 1;
    }
}

fn parse(input: &str) -> Vec<Hailstone> {
    input
        .lines()
        .map(|line| {
            let (pos, velocity) = line.split_once(" @ ").unwrap();
            Hailstone::new(Point3D::parse(pos), Point3D::parse(velocity))
        })
        .collect::<Vec<_>>()
}

struct Hailstone {
    pos: Point3D,
    velocity: Point3D,
}

impl Hailstone {
    fn new(pos: Point3D, velocity: Point3D) -> Self {
        Self { pos, velocity }
    }

    fn intersection(&self, other: &Self) -> Option<Point3D> {
        let (a1, b1) = self.ab();
        let (a2, b2) = other.ab();
        if a1 != a2 {
            let (x, y) = match (a1, a2) {
                (None, Some(a2)) => (self.pos.x, a2 * (self.pos.x - other.pos.x) + other.pos.y),
                (Some(a1), None) => (other.pos.x, a1 * (other.pos.x - self.pos.x) + self.pos.y),
                (Some(a1), Some(a2)) => {
                    let x = (b2 - b1) / (a1 - a2);
                    (x, a1 * x + b1)
                }
                _ => panic!(),
            };
            let (x, y) = (x.round(), y.round());

            if self.is_future(x) && other.is_future(x) {
                let t_self = self.get_t(y, x);
                let t_other = other.get_t(y, x);
                if t_self == t_other {
                    return None;
                }
                let z = (self.pos.z - other.pos.z + t_self * self.velocity.z
                    - t_other * other.velocity.z)
                    / (t_self - t_other);
                Some(Point3D::new(x, y, z.round()))
            } else {
                None
            }
        } else {
            None
        }
    }

    fn get_t(&self, y: Decimal, x: Decimal) -> Decimal {
        if self.velocity.y.is_zero() && self.velocity.x.is_zero() {
            return Decimal::zero();
        }
        if self.velocity.x.is_zero() {
            (y - self.pos.y) / self.velocity.y
        } else {
            (x - self.pos.x) / self.velocity.x
        }
    }

    fn is_future(&self, x: Decimal) -> bool {
        (x - self.pos.x).is_sign_negative() == self.velocity.x.is_sign_negative()
    }

    fn ab(&self) -> (Option<Decimal>, Decimal) {
        if self.velocity.x.is_zero() {
            (None, Decimal::one())
        } else {
            let a = self.velocity.y / self.velocity.x;
            let b = self.pos.y - a * self.pos.x;
            (Some(a), b)
        }
    }
}

#[derive(Copy, Clone, Debug, PartialEq)]
struct Point3D {
    x: Decimal,
    y: Decimal,
    z: Decimal,
}

impl Point3D {
    pub fn new(x: Decimal, y: Decimal, z: Decimal) -> Self {
        Self { x, y, z }
    }

    fn parse(input: &str) -> Self {
        let mut i = input.split(",");
        Self {
            x: i.next().unwrap().trim().parse().unwrap(),
            y: i.next().unwrap().trim().parse().unwrap(),
            z: i.next().unwrap().trim().parse().unwrap(),
        }
    }
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 24;

    const EXAMPLE: &str = "19, 13, 30 @ -2,  1, -2
18, 19, 22 @ -1, -1, -2
20, 25, 34 @ -2, -2, -4
12, 31, 28 @ -1, -2, -1
20, 19, 15 @  1, -5, -3";

    #[test]
    fn part1_example() {
        assert_eq!(
            test_support::do_part(|input| part1(input, 7.0, 7.0, 27.0, 27.0), EXAMPLE),
            2
        );
    }

    #[test]
    fn part1_solution() {
        assert_eq!(
            test_support::do_part(
                |input| part1(
                    input,
                    200000000000000.0,
                    200000000000000.0,
                    400000000000000.0,
                    400000000000000.0
                ),
                &read_input(DAY)
            ),
            17776
        );
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE), 47);
    }

    #[test]
    #[ignore]
    fn part2_solution() {
        assert_eq!(
            test_support::do_part(part2, &read_input(DAY)),
            948978092202212
        );
    }
}
