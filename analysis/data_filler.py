import pandas as pd
import numpy as np
import os
import time
import json
from tqdm import tqdm

config = json.load(open('config/config.json'))
total_time = config['simulationTime']

def fill_data(directory, output_directory):
    start_time = time.time()

    if not os.path.exists(output_directory):
        os.makedirs(output_directory)
    
    # Get all CSV files in the directory
    files = [f for f in os.listdir(directory) if f.endswith('_vel.csv')]
    
    for file in tqdm(files, desc="Processing files", unit="file"):
        file_path = os.path.join(directory, file)
        output_path = os.path.join(output_directory, file)
        
        # Specify dtypes upfront to avoid later conversions
        # Time,zombiePercentage,averageVelocity
        df = pd.read_csv(file_path, dtype={'Time': float, 'zombiePercentage': float, 'averageVelocity': float}, low_memory=False)
        
        # Add is_generated column with False for original values
        df['is_generated'] = False
        
        # Check if we need to generate new rows
        last_time = df['Time'].max()
        if last_time < total_time:
            # Compute timestep assuming uniform intervals
            # It's safer to compute from first two rows if consistent
            timestep = df['Time'].iloc[1] - df['Time'].iloc[0]

            # Calculate new times up to total_time, not exceeding it
            new_times = np.arange(last_time + timestep, total_time + timestep, timestep)
            
            if len(new_times) > 0:
                # Extract scalar values from the last row
                last_row = df.iloc[-1]
                zombie_pct = last_row['zombiePercentage']
                avg_vel = last_row['averageVelocity']
                
                # Directly construct the new DataFrame
                new_rows = pd.DataFrame({
                    'Time': new_times,
                    'zombiePercentage': zombie_pct,
                    'averageVelocity': avg_vel,
                    'is_generated': True
                })
                
                # Concatenate once
                df = pd.concat([df, new_rows], ignore_index=True)
                
                # If necessary, round Time (if not already suitable)
                df['Time'] = df['Time'].round(2)
                
        # Write back to CSV in the output directory
        df.to_csv(output_path, index=False)

    print(f'Total time taken: {round(time.time() - start_time, 2)}s')

def concatenate_data_per_probability(directory, output_directory):

    if not os.path.exists(output_directory):
        os.makedirs(output_directory)

    # Get all unique probabilities from filenames
    files = [f for f in os.listdir(directory) if f.endswith('_vel.csv')]
    probabilities = sorted(set(f.split('_')[1] for f in files))

    # Set dtypes to ensure consistent and efficient reading
    dtypes = {
        'Time': np.float64,
        'zombiePercentage': np.float64,
        'averageVelocity': np.float64,
        'is_generated': bool
    }

    # Process one probability at a time
    for probability in tqdm(probabilities, desc="Processing probabilities", unit="probability"):
        # Get files for this probability
        prob_files = [f for f in files if f.split('_')[1] == probability]
        dfs = []

        # Process files for current probability
        for file in prob_files:
            parts = file.split('_')
            realization = parts[2]
            file_path = os.path.join(directory, file)

            # Read CSV with specified dtypes
            df = pd.read_csv(file_path, dtype=dtypes, low_memory=False)
            
            # Add is_generated column if missing
            if 'is_generated' not in df.columns:
                df['is_generated'] = False

            # Add realization column
            df['realization'] = realization
            dfs.append(df)

        # Concatenate and save files for current probability
        concatenated_df = pd.concat(dfs, ignore_index=True)
        output_file = os.path.join(output_directory, f'concatenated_{probability}_vel.csv')
        concatenated_df.to_csv(output_file, index=False)
        
        # Free memory
        del dfs
        del concatenated_df

    print("Data concatenation per probability completed.")


if __name__ == "__main__":
    # fill_data("simulation_results/final_analysis", "simulation_results/final_analysis_filled")
    # concatenate_data_per_probability("simulation_results/final_analysis_filled", "simulation_results/final_analysis_filled_concatenated")
    fill_data("simulation_results/final_miti_miti_analysis", "simulation_results/final_miti_miti_analysis_filled")
    concatenate_data_per_probability("simulation_results/final_miti_miti_analysis_filled", "simulation_results/final_miti_miti_analysis_filled_concatenated")