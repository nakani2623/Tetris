#include "sdl_renderer.h"
#include <thread>
#include <chrono>
extern "C" SDL_Renderer *init_SDL(int width, int height) {
    SDL_Init(SDL_INIT_VIDEO);
    SDL_Window *window = SDL_CreateWindow(
        "SDL window",
        width, height, 0
    );
    SDL_Renderer *renderer = SDL_CreateRenderer(
        window, nullptr
    );
    return renderer;
}

extern "C" void simple_window(SDL_Renderer *renderer) {
    while (true) {
        std::this_thread::sleep_for(std::chrono::milliseconds(100));
        SDL_RenderPresent(renderer);
    }

}
