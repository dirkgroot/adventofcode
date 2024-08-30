use std::iter::successors;

/// Calculate the Least Common Multiple of two numbers.
/// See https://en.wikipedia.org/wiki/Least_common_multiple for more information.
///
/// # Examples
///
/// ```
/// use adventofcode_2023_rust::utils::lcm;
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
/// use adventofcode_2023_rust::utils::gcd;
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

/// Generate all possible permutations of a vector of elements, using Heap's algorithm.
/// See https://en.wikipedia.org/wiki/Heap%27s_algorithm for an explanation of this algorithm.
///
/// # Examples
///
/// ```
/// use adventofcode_2023_rust::utils::permutations;
///
/// let p = permutations(&vec![0, 1]);
/// assert_eq!(vec![0, 1], p[0]);
/// assert_eq!(vec![1, 0], p[1]);
/// ```
pub fn permutations<T>(v: &Vec<T>) -> Vec<Vec<T>>
where
    T: Clone,
    T: Copy,
{
    let mut work = v.clone();
    let mut result = Vec::new();
    fn gen<T>(k: usize, work: &mut Vec<T>, result: &mut Vec<Vec<T>>)
    where
        T: Clone,
        T: Copy,
    {
        if k == 1 {
            result.push(work.clone());
        } else {
            let k_minus_one = k - 1;
            gen(k_minus_one, work, result);
            for i in 0..k_minus_one {
                let swap_with = match k % 2 {
                    0 => i,
                    _ => 0,
                };
                let carry = work[swap_with];
                work[swap_with] = work[k_minus_one];
                work[k_minus_one] = carry;
                gen(k_minus_one, work, result);
            }
        }
    }
    gen::<T>(v.len(), &mut work, &mut result);

    result
}

#[derive(Clone, Copy, Eq, PartialEq, Ord, PartialOrd, Hash)]
pub enum Direction {
    N,
    E,
    S,
    W,
}

/// Parses a 2-dimensional map, represented as lines of text. Each line represents a row, every
/// character represents a cell.
///
/// # Examples
///
/// ```
/// use adventofcode_2023_rust::utils::parse_map;
///
/// assert_eq!(vec!(vec!('a', 'b'), vec!('c', 'd')), parse_map("ab\ncd"));
/// ```
pub fn parse_map(input: &str) -> Vec<Vec<char>> {
    input.lines().map(|line| line.chars().collect()).collect()
}
