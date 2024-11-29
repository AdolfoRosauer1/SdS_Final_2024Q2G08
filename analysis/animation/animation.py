import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.animation as animation
from pathlib import Path
from tqdm import tqdm  # Import tqdm for progress bar

def create_animation(csv_path: str, output_dir: str, fps: int = 30, duration_seconds: float = 10):
    """
    Creates an animation from a CSV file containing agent positions over time.
    
    Args:
        csv_path: Path to the CSV file
        output_dir: Directory where the output GIF will be saved
        fps: Frames per second for the animation
        duration_seconds: Duration of the animation in seconds
    """
    # Read the CSV file
    df = pd.read_csv(csv_path,
                    #  names=['Time', 'AgentID', 'AgentType', 'PosX', 'PosY', 'Radius'],
                     low_memory=False)
    
    # Convert numeric columns to float
    numeric_columns = ['Time', 'PosX', 'PosY', 'Radius']
    df[numeric_columns] = df[numeric_columns].astype(float)
    
    # Get unique timestamps
    timestamps = sorted(df['Time'].unique())
    
    # Create figure and axis
    fig, ax = plt.subplots(figsize=(10, 10))
    
    # Set fixed bounds based on data
    margin = 2  # Add some margin to the plot
    x_min, x_max = df['PosX'].min() - margin, df['PosX'].max() + margin
    y_min, y_max = df['PosY'].min() - margin, df['PosY'].max() + margin
    
    

    def animate(frame):
        # Clear previous frame
        ax.clear()
        
        # Get data for current timestamp
        current_frame = df[df['Time'] == timestamps[frame]]
        
        # Plot humans (blue) and zombies (red)
        humans = current_frame[current_frame['AgentType'] == 'HUMAN']
        zombies = current_frame[current_frame['AgentType'] == 'ZOMBIE']
        
        # Plot each agent type with their respective colors and sizes
        # Multiply radius by a scale factor to make agents more visible
        scale_factor = 1  # Adjust this value to make agents larger or smaller
        ax.scatter(humans['PosX'], humans['PosY'], c='blue', s=humans['Radius']*scale_factor, alpha=0.6, label='Human')
        ax.scatter(zombies['PosX'], zombies['PosY'], c='red', s=zombies['Radius']*scale_factor, alpha=0.6, label='Zombie')
        
        # Set consistent axis limits
        ax.set_xlim(x_min, x_max)
        ax.set_ylim(y_min, y_max)
        
        # Add title and legend
        ax.set_title(f'Time: {timestamps[frame]:.2f}s')
        ax.legend()
        ax.grid(True)
        
        # Set equal aspect ratio
        ax.set_aspect('equal')
    
    # Calculate number of frames based on duration and fps
    n_frames = min(len(timestamps), int(fps * duration_seconds))
    frame_interval = 1000 / fps  # interval in milliseconds
    
    progress_bar = tqdm(total=n_frames, desc="Animating frames")
    def animate_wrapper(frame):
        progress_bar.update(1)
        animate(frame)

    # Create animation with progress bar
    anim = animation.FuncAnimation(
        fig, 
        animate_wrapper,
        frames=n_frames,
        interval=frame_interval,
        repeat=False
    )
    
    # Create output directory if it doesn't exist
    output_path = Path(output_dir)
    output_path.mkdir(parents=True, exist_ok=True)
    
    # Save animation as GIF
    output_file = output_path / 'simulation.gif'
    anim.save(str(output_file), writer='pillow', fps=fps)
    plt.close()

if __name__ == "__main__":
    create_animation(
        csv_path="simulation_results/realization_1.csv",
        output_dir="analysis/animation/exports",
        fps=30,
        duration_seconds=10
    )

