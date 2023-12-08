use std::iter::successors;

/// Calculate the Least Common Multiple of two numbers.
/// See https://en.wikipedia.org/wiki/Least_common_multiple for more information.
///
/// # Examples
///
/// ```
/// use adventofcode_rust::utils::lcm;
///
/// assert_eq!(18, lcm(6, 9));
/// ```
pub fn lcm(a: u64, b: u64) -> u64 {
    a * (b / gcd(a, b))
}

/// Calculate the Greatest Common Divisor of two numbers, using the Euclidian algorithm.
/// See https://en.wikipedia.org/wiki/Euclidean_algorithm for an explanation of this algorithm.
///
/// # Examples
///
/// ```
/// use adventofcode_rust::utils::gcd;
///
/// assert_eq!(3, gcd(6, 9));
/// ```
pub fn gcd(a: u64, b: u64) -> u64 {
    successors(
        Some((a, b)),
        |(a, b)| if *b == 0 { None } else { Some((*b, a % b)) },
    )
    .last()
    .map(|(a, _)| a)
    .unwrap()
}
