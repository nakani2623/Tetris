package tetris.utils;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.file.Path;

public class SDL {
    private static final Linker linker = Linker.nativeLinker();

    private static final SymbolLookup lookup =
        SymbolLookup.libraryLookup(
            Path.of("libsdl_renderer.so").toAbsolutePath(),
            Arena.global()
        );

    private static MethodHandle load(String name, FunctionDescriptor descriptor) {
        MemorySegment symbol = lookup.find(name).orElseThrow(() ->
            new RuntimeException("Symbol not found: " + name)
        );

        return linker.downcallHandle(symbol, descriptor);
    }

    // start binding java methods to C ABI
    private static final MethodHandle init = load(
        "init_SDL",
        FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT)
    );
    public static MemorySegment init_SDL(int w, int h) {
        try {
            return (MemorySegment) init.invokeExact(w, h);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to call init_SDL", e);
        }
    }

    private static final MethodHandle simp_win = load(
        "simple_window",
        FunctionDescriptor.ofVoid(ValueLayout.ADDRESS)
    );

    public static void simp_win(MemorySegment rederer_ptr) {
        try {
            simp_win.invokeExact(rederer_ptr);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to call simp_win", e);
        }
    }

    private static final MethodHandle poll_event = load(
        "poll_event",
        FunctionDescriptor.of(ValueLayout.JAVA_INT)
    );

    public static int poll_event() {
        try {
            return (int)poll_event.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException("Failed to call poll_event", e);
        }
    }
}
