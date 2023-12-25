use rand::Rng;
use std::collections::{BTreeMap, BTreeSet};

pub fn part1(input: &str) -> usize {
    let edges = input
        .lines()
        .flat_map(|line| {
            let (src, dsts) = line.split_once(": ").unwrap();
            dsts.split_whitespace().map(move |dst| (src, dst))
        })
        .collect::<Vec<_>>();
    let vertices = edges
        .iter()
        .flat_map(|(a, b)| [*a, *b])
        .collect::<BTreeSet<_>>();

    loop {
        let (s1, s2, e) = min_cut(edges.clone(), vertices.clone());
        if e == 3 {
            return s1 * s2;
        }
    }
}

fn min_cut<'a>(
    mut edges: Vec<(&'a str, &'a str)>,
    mut vertices: BTreeSet<&str>,
) -> (usize, usize, usize) {
    let mut rng = rand::thread_rng();
    let mut groups = BTreeMap::new();

    while vertices.len() > 2 {
        let idx = rng.gen_range(0..edges.len());
        let (v_target, v_remove) = edges[idx];
        let remove_size = *groups.get(v_remove).unwrap_or(&1);

        edges.remove(idx);
        vertices.remove(v_remove);
        groups
            .entry(v_target)
            .and_modify(|x| *x += remove_size)
            .or_insert(1usize + remove_size);
        for (a, b) in edges.iter_mut() {
            if *a == v_remove {
                *a = v_target;
            }
            if *b == v_remove {
                *b = v_target;
            }
        }

        let mut i = 0;
        while i < edges.len() {
            let (a, b) = edges[i];
            if a == b {
                edges.remove(i);
            } else {
                i += 1;
            }
        }
    }

    let mut i = vertices.iter();

    let i1 = *groups.get(i.next().unwrap()).unwrap_or(&1);
    let i2 = *groups.get(i.next().unwrap()).unwrap_or(&1);
    (i1, i2, edges.len())
}

#[cfg(test)]
mod tests {
    use crate::test_support;
    use crate::year2023::read_input;

    use super::*;

    const DAY: i32 = 25;

    const EXAMPLE: &str = "jqt: rhn xhk nvd
rsh: frs pzl lsr
xhk: hfx
cmg: qnr nvd lhk bvb
rhn: xhk bvb hfx
bvb: xhk hfx
pzl: lsr hfx nvd
qnr: nvd
ntq: jqt hfx bvb xhk
nvd: lhk
lsr: lhk
rzs: qnr cmg lsr rsh
frs: qnr lhk lsr";

    #[test]
    fn part1_example() {
        assert_eq!(test_support::do_part(part1, EXAMPLE), 54);
    }

    #[test]
    fn part1_solution() {
        assert_eq!(test_support::do_part(part1, &read_input(DAY)), 543834);
    }
}
