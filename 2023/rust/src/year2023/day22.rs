use std::cmp::Ordering;
use std::collections::{BTreeSet, HashMap};

pub fn part1(input: &str) -> usize {
    let mut bricks = parse(input);
    let grid = create_grid(&mut bricks);
    let (support, supported_by) = collect_supports(&mut bricks, grid);

    (0..bricks.len())
        .filter(|i| {
            let s = &support[i];
            s.iter().all(|supported| supported_by[supported].len() > 1)
        })
        .count()
}

pub fn part2(input: &str) -> usize {
    let mut bricks = parse(input);
    let grid = create_grid(&mut bricks);
    let (support, supported_by) = collect_supports(&mut bricks, grid);

    (0..bricks.len())
        .map(|i| {
            chain_reaction(
                BTreeSet::from([i]),
                BTreeSet::from([i]),
                &support,
                &supported_by,
            )
        })
        .sum()
}

fn chain_reaction(
    bricks: BTreeSet<usize>,
    fallen: BTreeSet<usize>,
    supports: &HashMap<usize, BTreeSet<usize>>,
    supported_by: &HashMap<usize, BTreeSet<usize>>,
) -> usize {
    let mut supported_bricks = BTreeSet::new();
    bricks.iter().for_each(|i| {
        let s = &supports[i];
        supported_bricks.extend(s.iter());
    });

    let falling_bricks = supported_bricks
        .iter()
        .filter(|supported| supported_by[supported].iter().all(|s| fallen.contains(s)))
        .map(|it| *it)
        .collect::<BTreeSet<_>>();

    if falling_bricks.is_empty() {
        0
    } else {
        let mut fallen = fallen.clone();
        fallen.extend(&falling_bricks);
        falling_bricks.len() + chain_reaction(falling_bricks, fallen, supports, supported_by)
    }
}

fn parse(input: &str) -> Vec<Brick> {
    let mut bricks = input
        .lines()
        .map(|line| {
            let (a, b) = line.split_once("~").unwrap();
            Brick::new(Point3D::parse(a), Point3D::parse(b))
        })
        .collect::<Vec<_>>();
    bricks.sort();
    bricks
}

fn create_grid(bricks: &mut Vec<Brick>) -> Vec<Vec<Vec<usize>>> {
    let mut grid = vec![vec![vec![usize::MAX; 10]; 10]; 400];

    bricks.iter_mut().enumerate().for_each(|(i, brick)| {
        let z = (1usize..400usize)
            .rev()
            .take_while(|z| {
                (brick.a.y..=brick.b.y)
                    .all(|y| (brick.a.x..=brick.b.x).all(|x| grid[*z][y][x] == usize::MAX))
            })
            .last()
            .unwrap();
        let diff_z = brick.a.z - z;
        brick.a.z = z;
        brick.b.z -= diff_z;

        for z in brick.a.z..=brick.b.z {
            for y in brick.a.y..=brick.b.y {
                for x in brick.a.x..=brick.b.x {
                    grid[z][y][x] = i;
                }
            }
        }
    });

    grid
}

fn collect_supports(
    bricks: &mut Vec<Brick>,
    grid: Vec<Vec<Vec<usize>>>,
) -> (
    HashMap<usize, BTreeSet<usize>>,
    HashMap<usize, BTreeSet<usize>>,
) {
    let supports = bricks
        .iter()
        .enumerate()
        .map(|(i, brick)| {
            let support_z = brick.b.z + 1;
            let s = (brick.a.y..=brick.b.y)
                .flat_map(|y| {
                    (brick.a.x..=brick.b.x)
                        .map(|x| grid[support_z][y][x])
                        .collect::<Vec<_>>()
                })
                .filter(|it| *it != usize::MAX)
                .collect::<BTreeSet<_>>();
            (i, s)
        })
        .collect::<HashMap<_, _>>();

    let mut supported_by: HashMap<usize, BTreeSet<usize>> = HashMap::new();
    supports.iter().for_each(|(i, s)| {
        s.iter().for_each(|sup| {
            supported_by
                .entry(*sup)
                .and_modify(|a| {
                    a.insert(*i);
                })
                .or_insert(BTreeSet::from([*i]));
        });
    });

    (supports, supported_by)
}

#[derive(Eq, PartialEq)]
struct Brick {
    a: Point3D,
    b: Point3D,
}

impl Brick {
    pub fn new(a: Point3D, b: Point3D) -> Self {
        Self { a, b }
    }
}

impl Ord for Brick {
    fn cmp(&self, other: &Self) -> Ordering {
        self.a
            .z
            .cmp(&other.a.z)
            .then_with(|| self.a.y.cmp(&other.a.y))
            .then_with(|| self.a.x.cmp(&other.a.x))
    }
}

impl PartialOrd for Brick {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

#[derive(Eq, PartialEq, Debug)]
struct Point3D {
    x: usize,
    y: usize,
    z: usize,
}

impl Point3D {
    fn parse(input: &str) -> Self {
        let mut i = input.split(",").map(|i| i.parse().unwrap());
        Self {
            x: i.next().unwrap(),
            y: i.next().unwrap(),
            z: i.next().unwrap(),
        }
    }
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 22;

    const EXAMPLE: &str = "1,0,1~1,2,1
0,0,2~2,0,2
0,2,3~2,2,3
0,0,4~0,2,4
2,0,5~2,2,5
0,1,6~2,1,6
1,1,8~1,1,9";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE), 5);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 490);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE), 7);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 96356);
    }
}
