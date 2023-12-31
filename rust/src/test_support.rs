use std::fmt::Debug;
use std::fs;
use std::time::Instant;

pub fn read_input(year: i32, day: i32) -> String {
    let file = format!("input/{:04}/{:02}.txt", year, day);
    println!("Input   : {file}");
    fs::read_to_string(file).unwrap()
}

pub(crate) fn do_part<T>(p: fn(&str) -> T, input: &str) -> T
where
    T: Debug,
{
    let now = Instant::now();
    let result = p(input);
    let elapsed = now.elapsed();

    println!("Solution: {:?}", result);
    println!("Time    : {:.2?}", elapsed);
    println!("---");

    result
}
