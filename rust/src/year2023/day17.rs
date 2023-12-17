use crate::utils::Direction;
use std::cmp::Ordering;
use std::collections::{BinaryHeap, HashMap};

pub fn part1(input: &str) -> u32 {
    let map = parse(input);
    min_heat_loss(&map, 0, 0, map.len() - 1, map[0].len() - 1, false)
}

pub fn part2(input: &str) -> u32 {
    let map = parse(input);
    min_heat_loss(&map, 0, 0, map.len() - 1, map[0].len() - 1, true)
}

fn parse(input: &str) -> Vec<Vec<u32>> {
    input
        .lines()
        .map(|line| {
            line.chars()
                .map(|c| c.to_digit(10).unwrap())
                .collect::<Vec<_>>()
        })
        .collect::<Vec<_>>()
}

fn min_heat_loss(
    map: &Vec<Vec<u32>>,
    y1: usize,
    x1: usize,
    y2: usize,
    x2: usize,
    ultra: bool,
) -> u32 {
    let mut dist = vec![vec![u32::MAX; map[0].len()]; map.len()];
    let mut dist2: HashMap<(usize, usize, Direction, usize), u32> = HashMap::new();
    let mut queue = BinaryHeap::new();

    let state = State::new(0, y1, x1);
    dist[0][0] = 0;
    dist2.insert(state.key(), 0);
    queue.push(state);

    while let Some(u) = queue.pop() {
        if u.y == y2 && u.x == x2 {
            return u.cost;
        }

        if u.cost > dist2[&u.key()] {
            continue;
        }

        let neighbors = u.neighbors(map, ultra);
        for v in neighbors {
            if v.cost < *dist2.get(&v.key()).unwrap_or(&u32::MAX) {
                dist2.insert(v.key(), v.cost);
                queue.push(v);
            }
        }
    }

    0
}

#[derive(Copy, Clone, Eq, PartialEq)]
struct State {
    cost: u32,
    y: usize,
    x: usize,
    dir: Direction,
    steps: usize,
}

impl State {
    pub fn new(cost: u32, y: usize, x: usize) -> Self {
        Self {
            cost,
            y,
            x,
            dir: Direction::S,
            steps: 0,
        }
    }

    fn key(&self) -> (usize, usize, Direction, usize) {
        (self.y, self.x, self.dir, self.steps)
    }

    fn neighbors(&self, map: &Vec<Vec<u32>>, ultra: bool) -> Vec<State> {
        if ultra {
            self.neighbors_ultra(map)
        } else {
            self.neighbors_normal(map)
        }
    }

    fn neighbors_ultra(&self, map: &Vec<Vec<u32>>) -> Vec<State> {
        let mut result = Vec::new();
        if let Some(s) = self.next_ultra(map, Direction::N) {
            result.push(s);
        }
        if let Some(s) = self.next_ultra(map, Direction::E) {
            result.push(s);
        }
        if let Some(s) = self.next_ultra(map, Direction::S) {
            result.push(s);
        }
        if let Some(s) = self.next_ultra(map, Direction::W) {
            result.push(s);
        }
        result
    }

    fn next_ultra(&self, map: &Vec<Vec<u32>>, dir: Direction) -> Option<Self> {
        let new_steps = if dir == self.dir { self.steps + 1 } else { 1 };
        let may_turn = self.steps == 0 || self.steps >= 4;
        match dir {
            _ if new_steps > 10 => None,
            _ if dir != self.dir && !may_turn => None,
            Direction::N if self.dir != Direction::S => Some(Self {
                dir,
                y: self.y.checked_sub(1)?,
                steps: new_steps,
                cost: self.cost + map[self.y - 1][self.x],
                ..*self
            }),
            Direction::E if self.dir != Direction::W && self.x < map[0].len() - 1 => Some(Self {
                dir,
                x: self.x + 1,
                steps: new_steps,
                cost: self.cost + map[self.y][self.x + 1],
                ..*self
            }),
            Direction::S if self.dir != Direction::N && self.y < map.len() - 1 => Some(Self {
                dir,
                y: self.y + 1,
                steps: new_steps,
                cost: self.cost + map[self.y + 1][self.x],
                ..*self
            }),
            Direction::W if self.dir != Direction::E => Some(Self {
                dir,
                x: self.x.checked_sub(1)?,
                steps: new_steps,
                cost: self.cost + map[self.y][self.x - 1],
                ..*self
            }),
            _ => None,
        }
    }

    fn neighbors_normal(&self, map: &Vec<Vec<u32>>) -> Vec<State> {
        let mut result = Vec::new();
        if let Some(s) = self.next_normal(map, Direction::N) {
            result.push(s);
        }
        if let Some(s) = self.next_normal(map, Direction::E) {
            result.push(s);
        }
        if let Some(s) = self.next_normal(map, Direction::S) {
            result.push(s);
        }
        if let Some(s) = self.next_normal(map, Direction::W) {
            result.push(s);
        }
        result
    }

    fn next_normal(&self, map: &Vec<Vec<u32>>, dir: Direction) -> Option<Self> {
        let new_steps = if dir == self.dir { self.steps + 1 } else { 1 };
        match dir {
            _ if new_steps > 3 => None,
            Direction::N if self.dir != Direction::S => Some(Self {
                dir,
                y: self.y.checked_sub(1)?,
                steps: new_steps,
                cost: self.cost + map[self.y - 1][self.x],
                ..*self
            }),
            Direction::E if self.dir != Direction::W && self.x < map[0].len() - 1 => Some(Self {
                dir,
                x: self.x + 1,
                steps: new_steps,
                cost: self.cost + map[self.y][self.x + 1],
                ..*self
            }),
            Direction::S if self.dir != Direction::N && self.y < map.len() - 1 => Some(Self {
                dir,
                y: self.y + 1,
                steps: new_steps,
                cost: self.cost + map[self.y + 1][self.x],
                ..*self
            }),
            Direction::W if self.dir != Direction::E => Some(Self {
                dir,
                x: self.x.checked_sub(1)?,
                steps: new_steps,
                cost: self.cost + map[self.y][self.x - 1],
                ..*self
            }),
            _ => None,
        }
    }
}

impl Ord for State {
    fn cmp(&self, other: &Self) -> Ordering {
        other
            .cost
            .cmp(&self.cost)
            .then_with(|| self.x.cmp(&other.x))
            .then_with(|| self.y.cmp(&other.y))
            .then_with(|| self.dir.cmp(&other.dir))
            .then_with(|| self.steps.cmp(&other.steps))
    }
}

impl PartialOrd for State {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 17;

    const EXAMPLE: &str = "2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE), 102);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 665);
    }

    #[test]
    fn part2_example() {
        assert_eq!(test_support::do_part(part2, EXAMPLE), 94);
    }

    #[test]
    fn part2_solution() {
        assert_eq!(test_support::do_part(part2, &read_input(DAY)), 809);
    }
}
