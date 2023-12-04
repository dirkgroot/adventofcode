use std::iter::successors;
use std::slice::Iter;

use crate::year2019::intcode::Argmode::{Immediate, Position};

pub struct Intcode {
    ip: usize,
    mem: Vec<i32>,
}

impl Intcode {
    pub fn parse(input: &str) -> Intcode {
        Self {
            ip: 0,
            mem: input
                .split(",")
                .map(|i| i.parse::<i32>().unwrap())
                .collect::<Vec<i32>>(),
        }
    }

    pub fn get(&self, index: usize) -> i32 {
        self.mem[index]
    }

    pub fn set(&mut self, index: usize, value: i32) {
        self.mem[index] = value;
    }

    pub fn clone(&self) -> Intcode {
        Intcode {
            ip: self.ip,
            mem: self.mem.clone(),
        }
    }

    pub fn exec(&mut self, input: &Vec<i32>) -> Result<Vec<i32>, String> {
        let mut output = Vec::new();
        let mut input_iter = input.iter();
        loop {
            let opcode = self.next(Immediate);
            match opcode % 100 {
                1 => self.add(opcode),
                2 => self.multiply(opcode),
                3 => self.input(&mut input_iter),
                4 => self.output(opcode, &mut output),
                5 => self.jump_if_true(opcode),
                6 => self.jump_if_false(opcode),
                7 => self.less_than(opcode),
                8 => self.equals(opcode),
                99 => return Ok(output),
                _ => return Err(format!("Invalid opcode {}!", opcode)),
            }
        }
    }

    fn add(&mut self, opcode: i32) {
        let mut modes = Self::get_modes(opcode);
        let first = self.next(modes.next().unwrap());
        let second = self.next(modes.next().unwrap());
        let into = self.next(Immediate) as usize;
        self.mem[into] = first + second;
    }

    fn multiply(&mut self, opcode: i32) {
        let mut modes = Self::get_modes(opcode);
        let first = self.next(modes.next().unwrap());
        let second = self.next(modes.next().unwrap());
        let into = self.next(Immediate) as usize;
        self.mem[into] = first * second;
    }

    fn input(&mut self, input: &mut Iter<i32>) {
        let into = self.next(Immediate) as usize;
        self.mem[into] = *input.next().unwrap()
    }

    fn output(&mut self, opcode: i32, output: &mut Vec<i32>) {
        let mut modes = Self::get_modes(opcode);
        let first = self.next(modes.next().unwrap());
        output.push(first);
    }

    fn jump_if_true(&mut self, opcode: i32) {
        let mut modes = Self::get_modes(opcode);
        let first = self.next(modes.next().unwrap());
        let jump_to = self.next(modes.next().unwrap());
        if first != 0 {
            self.ip = jump_to as usize;
        }
    }

    fn jump_if_false(&mut self, opcode: i32) {
        let mut modes = Self::get_modes(opcode);
        let first = self.next(modes.next().unwrap());
        let jump_to = self.next(modes.next().unwrap());
        if first == 0 {
            self.ip = jump_to as usize;
        }
    }

    fn less_than(&mut self, opcode: i32) {
        let mut modes = Self::get_modes(opcode);
        let first = self.next(modes.next().unwrap());
        let second = self.next(modes.next().unwrap());
        let into = self.next(Immediate) as usize;
        self.mem[into] = if first < second { 1 } else { 0 }
    }

    fn equals(&mut self, opcode: i32) {
        let mut modes = Self::get_modes(opcode);
        let first = self.next(modes.next().unwrap());
        let second = self.next(modes.next().unwrap());
        let into = self.next(Immediate) as usize;
        self.mem[into] = if first == second { 1 } else { 0 }
    }

    fn next(&mut self, mode: Argmode) -> i32 {
        let val_or_address = self.mem[self.ip];
        self.ip += 1;
        match mode {
            Position => self.mem[val_or_address as usize],
            Immediate => val_or_address,
        }
    }
    fn get_modes(opcode: i32) -> impl Iterator<Item = Argmode> {
        successors(Some(opcode / 100), |m| Some(m / 10)).map(|i| match i % 10 {
            0 => Position,
            1 => Immediate,
            _ => panic!(),
        })
    }
}

enum Argmode {
    Position,
    Immediate,
}

#[cfg(test)]
mod tests {
    use crate::year2019::intcode::Intcode;

    #[test]
    fn test_add() {
        let mut intcode = Intcode::parse("1,5,6,0,99,2,3");
        intcode.exec(&vec![]).unwrap();
        assert_eq!(5, intcode.get(0))
    }

    #[test]
    fn test_multiply() {
        let mut intcode = Intcode::parse("2,5,6,0,99,2,3");
        intcode.exec(&vec![]).unwrap();
        assert_eq!(6, intcode.get(0))
    }

    #[test]
    fn test_immediate_args() {
        let mut intcode = Intcode::parse("1102,5,6,0,99");
        intcode.exec(&vec![]).unwrap();
        assert_eq!(30, intcode.get(0))
    }

    #[test]
    fn test_input() {
        let mut intcode = Intcode::parse("3,0,99");
        intcode.exec(&vec![10]).unwrap();
        assert_eq!(10, intcode.get(0))
    }

    #[test]
    fn test_output() {
        let mut intcode = Intcode::parse("4,0,99");
        let output = intcode.exec(&vec![10]).unwrap();
        assert_eq!(1, output.len());
        assert_eq!(4, output[0]);
    }

    #[test]
    fn test_jump_if_true() {
        let mut intcode = Intcode::parse("1105,1,5,104,0,104,1,99");
        let output = intcode.exec(&vec![]).unwrap();
        assert_eq!(1, output[0]);
        let mut intcode = Intcode::parse("1105,0,5,104,0,104,1,99");
        let output = intcode.exec(&vec![]).unwrap();
        assert_eq!(0, output[0]);
    }

    #[test]
    fn test_jump_if_false() {
        let mut intcode = Intcode::parse("1106,1,5,104,0,104,1,99");
        let output = intcode.exec(&vec![]).unwrap();
        assert_eq!(0, output[0]);
        let mut intcode = Intcode::parse("1106,0,5,104,0,104,1,99");
        let output = intcode.exec(&vec![]).unwrap();
        assert_eq!(1, output[0]);
    }

    #[test]
    fn test_less_than() {
        let mut intcode = Intcode::parse("1107,1,2,0,99");
        intcode.exec(&vec![]).unwrap();
        assert_eq!(1, intcode.get(0));
        let mut intcode = Intcode::parse("1107,2,1,0,99");
        intcode.exec(&vec![]).unwrap();
        assert_eq!(0, intcode.get(0));
    }

    #[test]
    fn test_equals() {
        let mut intcode = Intcode::parse("1108,1,1,0,99");
        intcode.exec(&vec![]).unwrap();
        assert_eq!(1, intcode.get(0));
        let mut intcode = Intcode::parse("1108,2,1,0,99");
        intcode.exec(&vec![]).unwrap();
        assert_eq!(0, intcode.get(0));
    }
}
