use std::fmt::Display;
use std::time::Instant;

pub(crate) fn do_part<T>(p: fn(&str) -> T, input: &str) -> T where T: Display {
    let now = Instant::now();
    let result = p(input);
    let elapsed = now.elapsed();

    println!("Solution: {result}");
    println!("Time    : {:.2?}", elapsed);

    result
}
