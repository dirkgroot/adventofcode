/// Generate all possible permutations of a vector of elements, using Heap's algorithm.
/// See https://en.wikipedia.org/wiki/Heap%27s_algorithm for an explanation of this algorithm.
///
/// # Examples
///
/// ```
/// use adventofcode_2019_rust::utils::permutations;
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
