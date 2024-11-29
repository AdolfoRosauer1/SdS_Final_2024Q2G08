# Time,AgentID,AgentType,PosX,PosY,Radius
# 0.0,0,ZOMBIE,-9.416326012279416,1.2580955569709449,0.5
# 0.0,1,HUMAN,-1.884477145090006,3.868480437822527,0.5
#
# Radius is a variable that determines the size of the circle at a given time.
# Position and radius are in the same units.
# The arena is a centered circle with radius 11
import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.animation as animation
import os
from matplotlib.patches import Circle
import numpy as np

def create_animation(csv_path, output_directory, fps, total_seconds):
    """
    Creates an animation from simulation data and saves it as a GIF.
    
    Args:
        csv_path (str): Path to the CSV file containing simulation data
        output_directory (str): Directory where the GIF will be saved
        fps (int): Frames per second for the animation
        total_seconds (float): Total duration of the animation in seconds
    """
    # Read the CSV file
    df = pd.read_csv(csv_path)
    
    # Get unique time steps from the data
    unique_times = sorted(df['Time'].unique())
    max_time = unique_times[-1]
    
    # Adjust total_seconds if it exceeds simulation time
    total_seconds = min(total_seconds, max_time)
    
    # Set up the figure with a specific size and black background
    fig, ax = plt.subplots(figsize=(10, 10), facecolor='black')
    ax.set_facecolor('black')
    
    # Set the arena boundaries (radius 11 as specified)
    ARENA_RADIUS = 11
    ax.set_xlim(-ARENA_RADIUS, ARENA_RADIUS)
    ax.set_ylim(-ARENA_RADIUS, ARENA_RADIUS)
    
    # Draw the arena boundary
    arena_boundary = Circle((0, 0), ARENA_RADIUS, fill=False, color='white', linestyle='--')
    ax.add_patch(arena_boundary)
    
    # Equal aspect ratio to ensure circles are round
    ax.set_aspect('equal')
    
    # Remove axes for cleaner visualization
    ax.set_xticks([])
    ax.set_yticks([])
    
    # Color mapping for agent types
    colors = {
        'HUMAN': 'blue',
        'ZOMBIE': 'red'
    }
    
    def find_nearest_time(target_time):
        """Find the nearest available time in the dataset"""
        idx = np.searchsorted(unique_times, target_time)
        if idx == 0:
            return unique_times[0]
        if idx == len(unique_times):
            return unique_times[-1]
        before = unique_times[idx - 1]
        after = unique_times[idx]
        if after - target_time < target_time - before:
            return after
        return before
    
    def update(frame):
        # Clear previous frame
        ax.clear()
        
        # Redraw arena boundary and settings
        ax.set_xlim(-ARENA_RADIUS, ARENA_RADIUS)
        ax.set_ylim(-ARENA_RADIUS, ARENA_RADIUS)
        ax.add_patch(Circle((0, 0), ARENA_RADIUS, fill=False, color='white', linestyle='--'))
        ax.set_aspect('equal')
        ax.set_xticks([])
        ax.set_yticks([])
        ax.set_facecolor('black')
        
        # Calculate target time and find nearest available time
        target_time = frame * (total_seconds / (fps * total_seconds))
        actual_time = find_nearest_time(target_time)
        
        # Get data for current time step
        current_frame = df[df['Time'] == actual_time]
        
        # Draw each agent
        for _, agent in current_frame.iterrows():
            circle = Circle(
                (agent['PosX'], agent['PosY']),
                radius=agent['Radius'],
                color=colors[agent['AgentType']],
                alpha=0.7
            )
            ax.add_patch(circle)
        
        # Add timestamp
        ax.text(
            -ARENA_RADIUS + 0.5, 
            ARENA_RADIUS - 1, 
            f'Time: {actual_time:.1f}s',
            color='white',
            fontsize=10
        )
    
    # Create the animation
    frames = int(fps * total_seconds)
    anim = animation.FuncAnimation(
        fig, 
        update,
        frames=frames,
        interval=1000/fps,  # interval in milliseconds
        blit=False
    )
    
    # Create output directory if it doesn't exist
    os.makedirs(output_directory, exist_ok=True)
    
    # Save the animation
    output_path = os.path.join(output_directory, 'simulation.gif')
    anim.save(
        output_path,
        writer='pillow',
        fps=fps,
        progress_callback=lambda i, n: print(f'Saving frame {i} of {n}')
    )
    
    plt.close()
    
    return output_path


output_path = create_animation(
    csv_path='simulation_results/realization_1.csv',
    output_directory='output/animations',
    fps=30,  # 30 frames per second
    total_seconds=10.0  # Total duration of the animation
)