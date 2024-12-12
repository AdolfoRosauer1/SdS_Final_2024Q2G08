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

def create_animation(csv_path, output_directory, filename, fps, start_time, total_seconds):
    """
    Creates an animation from simulation data and saves it as a GIF.
    
    Args:
        csv_path (str): Path to the CSV file containing simulation data
        output_directory (str): Directory where the GIF will be saved
        fps (int): Frames per second for the animation
        start_time (float): Start time of the animation in seconds
        total_seconds (float): Total duration of the animation in seconds
    """
    # Read the CSV file
    df = pd.read_csv(csv_path)
    
    # Get unique time steps from the data
    unique_times = sorted(df['Time'].unique())
    max_time = unique_times[-1]
    
    # Filter data to only include times after start_time
    df = df[df['Time'] >= start_time]
    unique_times = sorted(df['Time'].unique())
    
    # Adjust total_seconds if it exceeds simulation time
    total_seconds = min(total_seconds, max_time - start_time)
    
    # Set up the figure with a specific size and black background
    fig, ax = plt.subplots(figsize=(10, 10))
    
    # Set the arena boundaries (radius 11 as specified)
    ARENA_RADIUS = 11
    ax.set_xlim(-ARENA_RADIUS, ARENA_RADIUS)
    ax.set_ylim(-ARENA_RADIUS, ARENA_RADIUS)
    
    # Draw the arena boundary
    arena_boundary = Circle((0, 0), ARENA_RADIUS, fill=False, color='black', linestyle='--')
    ax.add_patch(arena_boundary)
    
    # Equal aspect ratio to ensure circles are round
    ax.set_aspect('equal')
    
    # Remove axes for cleaner visualization
    ax.set_xticks([])
    ax.set_yticks([])
    
    # Color mapping for agent types
    colors = {
        'HUMAN': 'blue',
        'ZOMBIE': 'green'
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
        ax.add_patch(Circle((0, 0), ARENA_RADIUS, fill=False, color='black', linestyle='--'))
        ax.set_aspect('equal')
        ax.set_xticks([])
        ax.set_yticks([])
        ax.set_facecolor('white')
        
        # Calculate target time and find nearest available time
        target_time = start_time + frame * (total_seconds / (fps * total_seconds))
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
            color='black',
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
    output_path = os.path.join(output_directory, filename+".gif")
    anim.save(
        output_path,
        writer='pillow',
        fps=fps,
        progress_callback=lambda i, n: print(f'Saving frame {i}/{n}', end='\r')
    )
    plt.close()
    
    return output_path


def create_animations(files:dict, amount_per_directory:int, fps:int, start_time:float, total_seconds:float):
    """Create animations from CSV files.
    
    Args:
        files (dict): Dictionary mapping directories to lists of CSV filenames
                     Schema: {directory: [file1, file2, file3, ...]}
        amount_per_directory (int): Number of animations to create per directory
        fps (int): Frames per second for animations
        start_time (float): Start time in seconds
        total_seconds (float): Total animation duration in seconds
    """
    for directory, filenames in files.items():
        # Create output subdirectory
        # exports/{directory}
        os.makedirs(os.path.join("analysis/exports", directory), exist_ok=True)
        
        # Create animations for subset of files
        for filename in filenames[:amount_per_directory]:
            create_animation(
                csv_path=os.path.join(directory, filename),
                output_directory=os.path.join("analysis/exports", directory), 
                filename=filename.split(".")[0],
                fps=fps,
                start_time=start_time,
                total_seconds=total_seconds
            )

def create_comparison_grid(csv_paths, output_path):
    """
    Creates a grid of snapshots from multiple simulations at different times.
    
    Args:
        csv_paths (list): List of CSV file paths to visualize
        output_path (str): Path where to save the resulting figure
    """
    fontsize = 14
    num_rows = len(csv_paths)
    num_cols = 3  # We always want 3 time snapshots per simulation
    
    # Set up the figure with a grid based on number of csv files
    fig, axs = plt.subplots(num_rows, num_cols, figsize=(4*num_cols, 4*num_rows))
    
    # Handle case of single row
    if num_rows == 1:
        axs = [axs]
    
    # Color mapping for agent types
    colors = {
        'HUMAN': 'blue',
        'ZOMBIE': 'green'
    }
    
    ARENA_RADIUS = 11
    
    for row, csv_path in enumerate(csv_paths):
        # Read data
        df = pd.read_csv(csv_path)
        unique_times = sorted(df['Time'].unique())
        max_time = unique_times[-1]
        
        # Extract probability from filename
        # Example: simulation_results/animaciones_finales/0,55/realization_0.55_9.csv
        directory = os.path.dirname(csv_path)
        prob_dir = os.path.basename(directory)
        probability = prob_dir.replace(',', '.')
        
        # Define snapshot times
        snapshot_times = [
            0.0,  # First frame
            20.0,  # Middle frame
            round(0.75 * max_time, 1)  # Last frame
        ]
        
        for col, target_time in enumerate(snapshot_times):
            # Find nearest available time
            actual_time = unique_times[np.searchsorted(unique_times, target_time)]
            current_frame = df[df['Time'] == actual_time]
            
            ax = axs[row][col]
            
            # Configure subplot
            ax.set_xlim(-ARENA_RADIUS, ARENA_RADIUS)
            ax.set_ylim(-ARENA_RADIUS, ARENA_RADIUS)
            ax.add_patch(Circle((0, 0), ARENA_RADIUS, fill=False, color='black'))
            ax.set_aspect('equal')
            ax.set_xticks([])
            ax.set_yticks([])
            
            # Plot agents
            for _, agent in current_frame.iterrows():
                circle = Circle(
                    (agent['PosX'], agent['PosY']),
                    radius=agent['Radius'],
                    color=colors[agent['AgentType']],
                    alpha=0.7
                )
                ax.add_patch(circle)
            
            # Add time label
            ax.text(
                -ARENA_RADIUS + 0.5,
                ARENA_RADIUS - 1,
                f'T={actual_time:.1f}s',
                fontsize=fontsize
            )
            
            # Add row label with probability
            if col == 0:
                ax.text(
                    -ARENA_RADIUS - 2,
                    0,
                    f'p = 0.5',
                    rotation=90,
                    verticalalignment='center',
                    fontsize=fontsize
                )
    
    plt.tight_layout()
    plt.savefig(output_path, dpi=300, bbox_inches='tight')
    plt.close()

import json
if __name__ == '__main__':
    # with open("analysis/realizations.json", "r") as f:
    #     files = json.load(f)
    # create_animations(files, 1, 30, 0.0, 100.0)
    # Example usage
    csv_paths = [
        # "simulation_results/animaciones_finales/0,55/realization_0.55_9.csv",
        # "simulation_results/animaciones_finales/0,7/realization_0.7_13.csv",
        # "simulation_results/animaciones_finales/1,0/realization_1.0_4.csv",
        "simulation_results/animaciones_finales/miti_miti/realization_0.5_1.csv"
    ]
    create_comparison_grid(csv_paths, "comparison_grid.png")