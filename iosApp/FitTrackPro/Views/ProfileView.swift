import SwiftUI
import shared

struct ProfileView: View {
    @EnvironmentObject var viewModel: SharedProfileViewModel
    @State private var isEditing = false
    
    var body: some View {
        NavigationView {
            if let profile = viewModel.profile {
                List {
                    Section(header: Text("Personal Information")) {
                        ProfileInfoRow(title: "Name", value: profile.name)
                        ProfileInfoRow(title: "Age", value: "\(profile.age)")
                        ProfileInfoRow(title: "Height", value: "\(profile.height) cm")
                        ProfileInfoRow(title: "Weight", value: "\(profile.weight) kg")
                    }
                    
                    Section(header: Text("Fitness Goals")) {
                        ProfileInfoRow(title: "Activity Level", value: profile.activityLevel)
                        ProfileInfoRow(title: "Weekly Goal", value: "\(profile.weeklyGoal) workouts")
                    }
                    
                    Section(header: Text("Preferences")) {
                        ForEach(profile.preferredWorkoutTypes, id: \.self) { type in
                            Text(type)
                        }
                    }
                }
                .navigationTitle("Profile")
                .toolbar {
                    ToolbarItem(placement: .navigationBarTrailing) {
                        Button(action: { isEditing = true }) {
                            Text("Edit")
                        }
                    }
                }
                .sheet(isPresented: $isEditing) {
                    EditProfileView(profile: profile)
                }
            } else {
                VStack {
                    Text("Create Your Profile")
                        .font(.title)
                        .padding()
                    
                    Button("Get Started") {
                        isEditing = true
                    }
                    .buttonStyle(.borderedProminent)
                }
                .sheet(isPresented: $isEditing) {
                    EditProfileView(profile: nil)
                }
            }
        }
    }
}

struct ProfileInfoRow: View {
    let title: String
    let value: String
    
    var body: some View {
        HStack {
            Text(title)
                .foregroundColor(.secondary)
            Spacer()
            Text(value)
        }
    }
}

struct EditProfileView: View {
    @Environment(\.dismiss) var dismiss
    @EnvironmentObject var viewModel: SharedProfileViewModel
    
    let profile: UserProfile?
    
    @State private var name: String
    @State private var age: Double
    @State private var height: Double
    @State private var weight: Double
    @State private var activityLevel: String
    @State private var weeklyGoal: Double
    @State private var preferredWorkoutTypes: Set<String>
    
    let activityLevels = ["Sedentary", "Light", "Moderate", "Active", "Very Active"]
    let workoutTypes = ["Running", "Walking", "Cycling", "Swimming", "Strength", "Yoga", "Other"]
    
    init(profile: UserProfile?) {
        self.profile = profile
        _name = State(initialValue: profile?.name ?? "")
        _age = State(initialValue: Double(profile?.age ?? 25))
        _height = State(initialValue: Double(profile?.height ?? 170))
        _weight = State(initialValue: Double(profile?.weight ?? 70))
        _activityLevel = State(initialValue: profile?.activityLevel ?? "Moderate")
        _weeklyGoal = State(initialValue: Double(profile?.weeklyGoal ?? 3))
        _preferredWorkoutTypes = State(initialValue: Set(profile?.preferredWorkoutTypes ?? ["Running", "Walking"]))
    }
    
    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("Personal Information")) {
                    TextField("Name", text: $name)
                    
                    HStack {
                        Text("Age")
                        Spacer()
                        TextField("Age", value: $age, format: .number)
                            .keyboardType(.numberPad)
                            .multilineTextAlignment(.trailing)
                        Text("years")
                    }
                    
                    HStack {
                        Text("Height")
                        Spacer()
                        TextField("Height", value: $height, format: .number)
                            .keyboardType(.numberPad)
                            .multilineTextAlignment(.trailing)
                        Text("cm")
                    }
                    
                    HStack {
                        Text("Weight")
                        Spacer()
                        TextField("Weight", value: $weight, format: .number)
                            .keyboardType(.numberPad)
                            .multilineTextAlignment(.trailing)
                        Text("kg")
                    }
                }
                
                Section(header: Text("Fitness Goals")) {
                    Picker("Activity Level", selection: $activityLevel) {
                        ForEach(activityLevels, id: \.self) { level in
                            Text(level).tag(level)
                        }
                    }
                    
                    HStack {
                        Text("Weekly Goal")
                        Spacer()
                        TextField("Weekly Goal", value: $weeklyGoal, format: .number)
                            .keyboardType(.numberPad)
                            .multilineTextAlignment(.trailing)
                        Text("workouts")
                    }
                }
                
                Section(header: Text("Preferred Workout Types")) {
                    ForEach(workoutTypes, id: \.self) { type in
                        Toggle(type, isOn: Binding(
                            get: { preferredWorkoutTypes.contains(type) },
                            set: { isSelected in
                                if isSelected {
                                    preferredWorkoutTypes.insert(type)
                                } else {
                                    preferredWorkoutTypes.remove(type)
                                }
                            }
                        ))
                    }
                }
            }
            .navigationTitle(profile == nil ? "Create Profile" : "Edit Profile")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") {
                        dismiss()
                    }
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button("Save") {
                        saveProfile()
                    }
                    .disabled(name.isEmpty || preferredWorkoutTypes.isEmpty)
                }
            }
        }
    }
    
    private func saveProfile() {
        let updatedProfile = UserProfile(
            id: profile?.id ?? UUID().uuidString,
            name: name,
            age: Int32(age),
            height: Int32(height),
            weight: Int32(weight),
            activityLevel: activityLevel,
            weeklyGoal: Int32(weeklyGoal),
            preferredWorkoutTypes: Array(preferredWorkoutTypes)
        )
        
        Task {
            if profile == nil {
                await viewModel.createProfile(updatedProfile)
            } else {
                await viewModel.updateProfile(updatedProfile)
            }
            dismiss()
        }
    }
}
