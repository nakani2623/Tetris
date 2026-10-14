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
    SDL_SetWindowKeyboardGrab(window, false);
    return renderer;
}

extern "C" void simple_window(SDL_Renderer *renderer) {
    bool running = true;
    int B = 50;
    bool incre = true;
    SDL_Event event;

    while (running) {
        std::this_thread::sleep_for(std::chrono::milliseconds(30));

        while (SDL_PollEvent(&event)) {
            if (event.type == SDL_EVENT_QUIT) {
                running = false;
            }
        }

        // Clear screen
        SDL_SetRenderDrawColor(
            renderer, 0, 0, 0, 255
        );
        SDL_RenderClear(renderer);

        // Draw a red rectangle
        SDL_FRect rect = {200, 2*B, 50, 50};
        if (incre)
            B++;
        else
            B--;
        
        if (B >=255)
            incre = false;
        if (B <= 0)
            incre = true;
        
        
        SDL_SetRenderDrawColor(
            renderer, 255, 0, B, 255
        );
        SDL_RenderFillRect(renderer, &rect);

        SDL_RenderPresent(renderer);
    }

}
