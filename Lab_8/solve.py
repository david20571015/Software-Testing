import angr
import claripy
from angrutils import plot_cfg


def dump_cfg():
    project = angr.Project('./target', load_options={'auto_load_libs': False})
    main = project.loader.main_object.get_symbol('main')
    start_state = project.factory.entry_state(addr=main.rebased_addr)
    cfg = project.analyses.CFGEmulated(fail_fast=True,
                                       starts=[main.rebased_addr],
                                       initial_state=start_state)
    plot_cfg(cfg,
             "target",
             asminst=False,
             remove_imports=True,
             remove_path_terminator=True)


def solve():
    project = angr.Project('./target', load_options={'auto_load_libs': False})

    sym_arg_size = 28
    sym_arg = claripy.BVS('sym_arg', 8 * sym_arg_size)

    argv = [project.filename]
    argv.append(sym_arg)
    state = project.factory.entry_state(args=argv)

    simgr = project.factory.simulation_manager(state)

    find_addr = 0x500028
    simgr.explore(find=find_addr)

    print(
        f'found: {simgr.found}\n'
        f'flags: {[found.solver.eval(sym_arg, cast_to=bytes) for found in simgr.found]}\n'
    )


if __name__ == '__main__':
    dump_cfg()
    solve()
