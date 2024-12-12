import matplotlib.pyplot as plt
import pandas as pd

fontsize = 14
linewidth = 2
plot_size = (10, 6)
cmap = plt.cm.tab10
general_color = 'green'
probabilities_map = {
    0.4: 'blue',
    0.45: 'orange',
    0.5: 'green',
    0.55: 'red',
    0.6: 'purple',
    0.7: 'brown',
    1.0: 'gray'
}

def get_color(probability):
    # Si probability está en el mapa, retornamos el color del mapa.
    # Si no, retornamos el color general.
    return probabilities_map.get(probability, general_color)


def plot_zombie_percentage_over_time(df, labels: bool = False, amount: int = 25):
    # Graficar el porcentaje de zombis en función del tiempo para todas las realizaciones
    plt.figure(figsize=plot_size)
    # Aquí no se usan probabilidades, así que utilizamos el color general.
    for realization in df['realization'].unique()[:amount]:
        realization_data = df[df['realization'] == realization]
        plt.plot(realization_data['Time'], realization_data['zombiePercentage'], 
                 label=f'Realización {realization}',
                #  color=general_color,
                   linewidth=linewidth)
    max_time = df['Time'].max()
    plt.xlabel('Tiempo (s)', fontsize=fontsize)
    plt.ylabel('Porcentaje de Zombies (%)', fontsize=fontsize)
    plt.ylim(-0.05, 1.05)
    plt.xlim(0, 2050)
    if labels:
        plt.legend(fontsize=fontsize)
    plt.show()

def plot_mean_zombie_percentage_over_time(df):
    grouped = df.groupby('Time')['zombiePercentage'].mean()
    sem = df.groupby('Time')['zombiePercentage'].sem()
    plt.figure(figsize=plot_size)
    # Aquí tampoco hay probabilidades, un solo color general
    plt.plot(grouped.index, grouped.values, color=general_color, linewidth=linewidth)
    # Add error bands
    plt.fill_between(grouped.index, grouped.values - sem, grouped.values + sem, alpha=0.3, color=general_color)

    plt.xlabel('Tiempo (s)', fontsize=fontsize)
    plt.ylabel('Porcentaje de Zombies (%)', fontsize=fontsize)
    plt.ylim(-0.05, 1.05)
    plt.xlim(0, 2050)
    plt.show()

def plot_mean_zombie_percentage_over_time_multiple_dfs(dfs: list[pd.DataFrame], probabilities: list[float]):
    plt.figure(figsize=plot_size)
    max_time = max(df['Time'].max() for df in dfs)
    # dfs y probabilities tienen correspondencia 1 a 1
    for df, probability in zip(dfs, probabilities):
        color = get_color(probability)
        grouped = df.groupby('Time')['zombiePercentage'].mean()
        sem = df.groupby('Time')['zombiePercentage'].sem()
        plt.plot(grouped.index, grouped.values, color=color, linewidth=linewidth, label=f'p = {probability}')
        plt.fill_between(grouped.index, grouped.values - sem, grouped.values + sem, alpha=0.3, color=color)
    plt.xlabel('Tiempo (s)', fontsize=fontsize)
    plt.ylabel('Porcentaje de Zombies (%)', fontsize=fontsize)
    plt.ylim(-0.05, 1.05)
    plt.xlim(0, 2050)
    plt.legend(fontsize=fontsize)
    plt.show()

def plot_comparison_between_probabilities_single_df(df: pd.DataFrame):
    plt.figure(figsize=plot_size)
    
    probabilities = df['probability'].unique()
    
    for probability in probabilities:
        color = get_color(probability)
        prob_df = df[df['probability'] == probability]
        
        non_generated_df = prob_df[prob_df['is_generated'] == False]
        max_times = non_generated_df.groupby('realization')['Time'].max()
        max_time_realizations = max_times[max_times == max_times.max()].index
        
        if len(max_time_realizations) > 1:
            mean_percentages = non_generated_df[non_generated_df['realization'].isin(max_time_realizations)].groupby('realization')['zombiePercentage'].mean()
            realization = (mean_percentages - 0.5).abs().idxmin()
        else:
            realization = max_time_realizations[0]
            
        realization_data = prob_df[prob_df['realization'] == realization]
        plt.plot(realization_data['Time'], realization_data['zombiePercentage'],
                 color=color,
                 label=f'Probabilidad {probability}', linewidth=linewidth)
                
    max_time = df['Time'].max()
    plt.xlabel('Tiempo (s)', fontsize=fontsize)
    plt.ylabel('Porcentaje de Zombies (%)', fontsize=fontsize)
    plt.ylim(-0.05, 1.05)
    plt.xlim(0, 2050)
    plt.legend(fontsize=fontsize)
    plt.show()

def plot_comparison_between_probabilities(dfs: list[pd.DataFrame], probabilities: list[float]):
    plt.figure(figsize=plot_size)
    
    for df, probability in zip(dfs, probabilities):
        color = get_color(probability)
        non_generated_df = df[df['is_generated'] == False]
        max_times = non_generated_df.groupby('realization')['Time'].max()
        max_time_realizations = max_times[max_times == max_times.max()].index
        
        if len(max_time_realizations) > 1:
            mean_percentages = non_generated_df[non_generated_df['realization'].isin(max_time_realizations)].groupby('realization')['zombiePercentage'].mean()
            realization = (mean_percentages - 0.5).abs().idxmin()
        else:
            realization = max_time_realizations[0]
            
        realization_data = df[df['realization'] == realization]
        plt.plot(realization_data['Time'], realization_data['zombiePercentage'],
                 color=color,
                 label=f'Probabilidad {probability}', linewidth=linewidth)
    max_time = max(df['Time'].max() for df in dfs)
    plt.xlabel('Tiempo (s)', fontsize=fontsize)
    plt.ylabel('Porcentaje de Zombies (%)', fontsize=fontsize)
    plt.ylim(-0.05, 1.05)
    plt.xlim(0, 2050)
    plt.legend(fontsize=fontsize)
    plt.show()

def plot_mean_zombie_percentage_vs_probability_single_df(df):
    filtered_df = df[df['Time'] > 250]
    final_percentages = filtered_df.groupby('realization')['zombiePercentage'].last()
    
    mean_percentage = final_percentages.mean()
    sem = final_percentages.sem()
    
    plt.figure(figsize=plot_size)
    # Aquí solo se grafica un punto, si la df tiene una sola probabilidad, usamos su color
    # En caso de que no esté en el mapa, se usa el color general.
    probability = df['probability'].iloc[0]
    color = get_color(probability)
    plt.errorbar(probability, mean_percentage, yerr=sem, 
                 fmt='o', color=color, capsize=5, capthick=2, markersize=8)
    
    plt.xlabel('Probabilidad de infección', fontsize=fontsize)
    plt.ylabel('Porcentaje final de Zombies (%)', fontsize=fontsize)
    plt.ylim(0, 1)
    plt.show()
    
def plot_velocity_over_time(df, labels: bool = False):
    plt.figure(figsize=plot_size)
    df = df[~df['is_generated']]
    # Aquí no hay probabilidades, color general
    for realization in df['realization'].unique():
        realization_data = df[df['realization'] == realization]
        plt.plot(realization_data['Time'], realization_data['averageVelocity'], 
                 label=f'Realización {realization}', color=general_color, linewidth=linewidth)
    plt.xlabel('Tiempo (s)', fontsize=fontsize)
    plt.ylabel('Velocidad promedio (m/s)', fontsize=fontsize)
    if labels:
        plt.legend(fontsize=fontsize)
    plt.show()

def plot_velocity_over_time_mean(df):
    df = df[~df['is_generated']]
    grouped = df.groupby('Time')['averageVelocity'].mean()
    sem = df.groupby('Time')['averageVelocity'].sem()
    plt.figure(figsize=plot_size)
    # Aquí no hay probabilidades, color general
    plt.plot(grouped.index, grouped.values, color=general_color, linewidth=linewidth)
    plt.fill_between(grouped.index, grouped.values - sem, grouped.values + sem, alpha=0.3, color=general_color)
    plt.xlabel('Tiempo (s)', fontsize=fontsize)
    plt.ylabel('Velocidad promedio (m/s)', fontsize=fontsize)
    plt.show()

def plot_mean_velocity_over_time_multiple_dfs(dfs: list[pd.DataFrame], probabilities: list[float]):
    plt.figure(figsize=plot_size)
    for df, probability in zip(dfs, probabilities):
        color = get_color(probability)
        grouped = df.groupby('Time')['averageVelocity'].mean()
        sem = df.groupby('Time')['averageVelocity'].sem()
        plt.plot(grouped.index, grouped.values, color=color, linewidth=linewidth, label=f'p = {probability}')
        plt.fill_between(grouped.index, grouped.values - sem, grouped.values + sem, alpha=0.3, color=color)
    plt.xlabel('Tiempo (s)', fontsize=fontsize)
    plt.ylabel('Velocidad promedio (m/s)', fontsize=fontsize)
    plt.legend(fontsize=fontsize)
    plt.show()

def plot_probability_vs_zombie_percentage(dfs: list[pd.DataFrame], probabilities: list[float]):
    # x axis: probability, y axis: mean zombie percentage in the max time (Time = max_time)
    # TODO: no connecting line
    mean_values = []
    error_values = []
    for df, probability in zip(dfs, probabilities):
        max_time = df['Time'].max()
        final_time_df = df[df['Time'] == max_time]
        mean_value = final_time_df['zombiePercentage'].mean()
        error_value = final_time_df['zombiePercentage'].sem()
        mean_values.append(mean_value)
        error_values.append(error_value)

    plt.figure(figsize=plot_size)
    # Primero, dibujamos una línea base con el color general para conectar todos los puntos
    # plt.plot(probabilities, mean_values, color=general_color, linewidth=linewidth)

    # Ahora dibujamos cada punto individualmente con su barra de error y color específico.
    for p, m, e in zip(probabilities, mean_values, error_values):
        color = get_color(p)
        plt.errorbar(p, m, yerr=e, fmt='o', color=color, capsize=5, capthick=2, markersize=8, label=f'p = {p}')

    plt.xlabel('Probabilidad de infección', fontsize=fontsize)
    plt.ylabel('Porcentaje final de Zombies (%)', fontsize=fontsize)
    # plt.legend(fontsize=fontsize)
    plt.ylim(-0.05, 1.05)
    plt.show()

def plot_probability_vs_velocity(dfs: list[pd.DataFrame], probabilities: list[float]):
    # x axis: probability, y axis: mean velocity in the max time (Time = max_time)
    # TODO: no connecting line
    mean_values = []
    error_values = []
    for df, probability in zip(dfs, probabilities):
        max_time = df['Time'].max()
        final_time_df = df[df['Time'] == max_time]
        mean_value = final_time_df['averageVelocity'].mean()
        error_value = final_time_df['averageVelocity'].sem()
        mean_values.append(mean_value)
        error_values.append(error_value)

    plt.figure(figsize=plot_size)
    # Línea base con color general
    # plt.plot(probabilities, mean_values, color=general_color, linewidth=linewidth)

    # Puntos individuales con colores específicos
    for p, m, e in zip(probabilities, mean_values, error_values):
        color = get_color(p)
        plt.errorbar(p, m, yerr=e, fmt='o', color=color, capsize=5, capthick=2, markersize=8, label=f'p = {p}')

    plt.xlabel('Probabilidad de infección', fontsize=fontsize)
    plt.ylabel('Velocidad promedio (m/s)', fontsize=fontsize)
    # plt.legend(fontsize=fontsize)
    plt.ylim(0, 4.5)
    plt.show()


def plot_all(df: dict):
    plot_zombie_percentage_over_time(df)
    plot_mean_zombie_percentage_over_time(df)
    plot_velocity_over_time(df)
    plot_velocity_over_time_mean(df)
