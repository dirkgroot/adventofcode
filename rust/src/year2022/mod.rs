pub mod day00;
pub mod day01;
pub mod day02;
pub mod day03;

#[cfg(test)]
fn read_input(day: i32) -> String {
    crate::test_support::read_input(2022, day)
}
